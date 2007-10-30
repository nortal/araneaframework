package org.araneaframework.example.main.release.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.util.FormUtil;
import org.araneaframework.uilib.util.MessageUtil;

public class SimpleInMemoryEditableList extends TemplateBaseWidget {
	private static final long serialVersionUID = 1L;
	protected List friends = new ArrayList();

	private MemoryBasedListDataProvider dataProvider = new DataProvider();

	private BeanFormListWidget editableRows;

	//Plays the role of a sequence
	private Long lastId =  new Long(0);

	{
		Random rn = new Random();
		List allSuggestions = new ArrayList();

		for (Iterator i = Arrays.asList(Locale.getISOCountries()).iterator(); i.hasNext(); ) {
			allSuggestions.add(new Locale("en", (String)i.next()).getDisplayCountry(Locale.ENGLISH));
		}

		for (int i = 0; i <  ExampleData.males.length; i++) {
			ExampleData.Client friend = new ExampleData.Client();
			friend.setForename(ExampleData.males[i]);
			friend.setId(lastId);
			friend.setSex("M");
			friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
			friend.setCountry((String)allSuggestions.get(rn.nextInt(allSuggestions.size())));
			friends.add(friend);
			lastId = new Long(lastId.longValue() + 1);
		}

		for (int i = 0; i <  ExampleData.females.length; i++) {
			ExampleData.Client friend = new ExampleData.Client();
			friend.setForename(ExampleData.females[i]);
			friend.setSex ("F");
			friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
			friend.setCountry((String)allSuggestions.get(rn.nextInt(allSuggestions.size())));
			friends.add(friend);
			lastId = new Long(lastId.longValue() + 1);
		}
	}

	/* Editable list. */ 
	private EditableBeanListWidget list;
	/* Actual holder of editable list rows (resides inside EditableBeanListWidget).
       Look inside init() method to see where it comes from. */ 
	private BeanFormListWidget formList;

	protected void init() throws Exception {
		setViewSelector("release/features/simpleEditableList/simpleInmemoryEditableList");

		/* PersonMO class is already familiar from form examples. 
       FormRowHandler class that will handle the different row operations. */
		list = new EditableBeanListWidget(new FriendEditableRowHandler(), ExampleData.Client.class);
		this.formList = list.getFormList();
		addWidget("list", list);
		list.setOrderableByDefault(true);
		//list.addField("id", "#Id", false);
		/* Filtering by fields other than ID is enabled. */
		list.addField("sex", "sed.Sex").like();		
		list.addField("forename", "sed.Forename").like();
		list.addField("surname", "sed.Surname").like();
		list.addField("country", "common.Country").like();
		list.addField("dummy", null, false);

		list.setDataProvider(dataProvider);
	}

	private class DataProvider extends MemoryBasedListDataProvider {
		private static final long serialVersionUID = 1L;
		protected DataProvider() {
			super(ExampleData.Client.class);
		}
		public List loadData() throws Exception {
			return friends;
		}
	}

	private class FriendEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
		private static final long serialVersionUID = 1L;

		public FriendEditableRowHandler() {}

		public Object getRowKey(Object rowData) {
			return ((ExampleData.Client) rowData).getId();
		}

		public void saveValidRow(FormRow editableRow) throws Exception {
			ExampleData.Client rowData = (ExampleData.Client) ((BeanFormWidget)editableRow.getForm()).writeToBean(new ExampleData.Client());
			rowData.setId((Long) editableRow.getKey());

			ExampleData.Client toModify = (ExampleData.Client) CollectionUtils.find(friends, new BeanPropertyValueEqualsPredicate("id", editableRow.getKey()));
			toModify.edit(rowData);

			editableRow.close();
		}

		public void deleteRow(Object key) throws Exception {
			Long id = (Long) key;
			ExampleData.Client toRemove = (ExampleData.Client) CollectionUtils.find(friends, new BeanPropertyValueEqualsPredicate("id", id));
			friends.remove(toRemove);
			
			list.getDataProvider().refreshData();
		}

		public void addValidRow(FormWidget addForm) throws Exception {
			ExampleData.Client newFriend = (ExampleData.Client) (((BeanFormWidget)addForm).writeToBean(new ExampleData.Client()));
			friends.add(newFriend);
			list.getDataProvider().refreshData();
			formList.resetAddForm();
		}

		// Called to initialize each row in editable list.
		public void initFormRow(FormRow editableRow, Object rowData) throws Exception {
			editableRow.close();

			BeanFormWidget rowForm = (BeanFormWidget)editableRow.getForm();
			addCommonFormFields(rowForm);
			FormListUtil.addEditSaveButtonToRowForm("#", formList, rowForm, getRowKey(rowData));
			FormListUtil.addDeleteButtonToRowForm("#", formList, rowForm, getRowKey(rowData));

			rowForm.readFromBean(rowData);
		}

		public void initAddForm(FormWidget addForm) throws Exception {
			addCommonFormFields((BeanFormWidget)addForm);
			FormListUtil.addAddButtonToAddForm("#", formList, addForm);
		}

		private void addCommonFormFields(BeanFormWidget form) throws Exception {
			form.addElement("sex", buildSexElement());
			form.addBeanElement("forename", "sed.Forename", new TextControl(), true);
			form.addBeanElement("surname", "sed.Surname", new TextControl(),  true);
			form.addBeanElement("country", "common.Country", new TextControl(), true);
		}
	}
	
	private FormElement buildSexElement() {
		FormElement result = FormUtil.createElement("sed.Sex", new TextControl(), new StringData(), true);
		result.setConstraint(new SexConstraint());

		return result;
	}
	private class SexConstraint extends BaseFieldConstraint {
		public SexConstraint() {}

		protected void validateConstraint() throws Exception {
			String value = (String) getValue();
			if (!("m".equals(value.toLowerCase()) || "f".equals(value.toLowerCase()))) {
				addError(
						MessageUtil.localizeAndFormat("sed.sexcon.ermsg", t(getLabel()), getEnvironment()));
			}
		}
	}
}

