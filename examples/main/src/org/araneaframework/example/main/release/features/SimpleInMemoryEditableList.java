package org.araneaframework.example.main.release.features;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.FormRowHandler;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

public class SimpleInMemoryEditableList extends TemplateBaseWidget {
	private static final long serialVersionUID = 1L;
	protected List friends = new ArrayList();

	private MemoryBasedListDataProvider dataProvider = new DataProvider();

	private BeanFormListWidget editableRows;

	//Plays the role of a sequence
	private Long lastId = Long.valueOf(0);

	private static final String males[] = { "Adar","Arel","Arhe","Daniel-Bhekiwe","Dimon","Georg-Goswin","Gomer","Greito","Hasgeir-Egert","Indre","Indy","Ino","Jamo","Japheth","Jeonel","Jorke Patrick","Kellven","Kendris","Kenomar","Kösta","Luukas Aeg","Matrix","Norton","Oleander","Radu","Rato","Relon","Rhening","Riko-Givy","Ruud","Signor","Taigo Juunior","Tristan Timothy","Uko Hiid","Örö Tormi"};
	private static final String females[] = {"Aaria Lee","Berlyka","Bretie","Cayro-Lyza","Cameron","Carlyne","Carmelyta","Chantell","Darelyan","Emmelgelina","Genivive","Grittel","Gynethy","Hailely","Haldja","Hertrudis","Isis","Jonete","Kaleria","Karetti","Keira","Kirteli","Kärg","Lomely","Lorelai","Madara Kuld","Marimar","Mirjam Sürr","Nia","Patrina","Pipi Triinu","Priti","Randelyn","Reneeta","Rinell","Rhe","Sarmika-Hell","Sevinc","Sibell","Sisandra","Viive-Virhiinya","Virbi Kleer"};
	private static final String fungi[] = {
		"Männi-kivipuravik",
		"Männiliimik",
		"Kobarkõrges",
		"Rõngasvöödik",
		"Kollanutt",
		"Narmasnutt",
		"Kastan-narmasnutt",
		"Kuhik-narmasnutt",
		"Lakkrupik",
		"Kamperriisikas",
		"Sooriisikas",
		"Näsariisikas",
		"Tõmmuriisikas",
		"Männiriisikas",
		"Tavariisikas",
		"Hallipiimane",
		"Kasepuravik",
		"Suursirmik",
		"Sametvahelik",
		"Tavavahelik",
		"Leekmampel",
		"Soomusmampel",
		"Kitsemampel",
		"Kasepilvik",
		"Soopilvik",
		"Kuld-puiduheinik",
		"Sapipuravik",
		"Pruunsamet",
		"Koorikkulised",
		"Kasepehik",
		"Majamädik",
		"Hõbelehisus",
		"Tuletael",
		"Kännupess",
		"Jänesvaabik",
		"Mustpässik",
		"Vahtratarjak",
		"Juurepruunik",
		"Must",
		"Männitaelik",
		"Täpptael",
		"Haavataelik",
		"Kasekäsnak",
		"Karvane",
		"Lehternahkes",
		"Kuusekõbjuk"
	};

	{
		Random rn = new Random();

		for (int i = 0; i <  males.length; i++) {
			Friend friend = new Friend();
			friend.setForename(males[i]);
			friend.setId(lastId);
			friend.setSex("M");
			friend.setSurname(fungi[rn.nextInt(fungi.length)]);
			friends.add(friend);
			lastId = Long.valueOf(lastId.longValue() + 1);
		}

		for (int i = 0; i <  females.length; i++) {
			Friend friend = new Friend();
			friend.setForename(females[i]);
			friend.setSex ("F");
			friend.setSurname(fungi[rn.nextInt(fungi.length)]);
			friends.add(friend);
			lastId = Long.valueOf(lastId.longValue() + 1);
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
		list = new EditableBeanListWidget(new FriendEditableRowHandler(), Friend.class);
		this.formList = list.getFormList();
		addWidget("list", list);
		list.setOrderableByDefault(true);
		list.addField("id", "#Id", false);
		/* Filtering by fields other than ID is enabled. */
		list.addField("forename", "#First name").like();
		list.addField("surname", "#Last name").like();
		list.addField("sex", "#Phone no").like();		
		list.addField("dummy", null, false);

		list.setDataProvider(buildListDataProvider());
	}


	protected ListDataProvider buildListDataProvider() throws Exception {
		return dataProvider;
	}

	protected FormRowHandler buildFormRowHandler() throws Exception {
		return new FriendEditableRowHandler();
	}

	private class DataProvider extends MemoryBasedListDataProvider {
		private static final long serialVersionUID = 1L;
		protected DataProvider() {
			super(Friend.class);
		}
		public List loadData() throws Exception {
			return friends;
		}
	}

	private class FriendEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
		private static final long serialVersionUID = 1L;

		public FriendEditableRowHandler() {}


		public Object getRowKey(Object rowData) {
			return ((Friend) rowData).getId();
		}

		public void saveValidRow(FormRow editableRow) throws Exception {
			Friend rowData = (Friend) ((BeanFormWidget)editableRow.getForm()).writeToBean(new Friend());
			rowData.setId((Long) editableRow.getKey());

			Friend toModify = (Friend) CollectionUtils.find(friends, new BeanPropertyValueEqualsPredicate("id", editableRow.getKey()));
			toModify.edit(rowData);

			editableRow.close();
		}

		public void deleteRow(Object key) throws Exception {
			Long id = (Long) key;
			Friend toRemove = (Friend) CollectionUtils.find(friends, new BeanPropertyValueEqualsPredicate("id", id));
			friends.remove(toRemove);
			
			list.getDataProvider().refreshData();
		}

		public void addValidRow(FormWidget addForm) throws Exception {
			Friend newFriend = (Friend) (((BeanFormWidget)addForm).writeToBean(new Friend()));
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
			form.addBeanElement("forename", "#String field", new TextControl(), true);
			form.addBeanElement("surname", "#Long field", new TextControl(),  true);
			form.addBeanElement("sex", "#Boolean field", new TextControl(), true);
		}
	}


//	public void init() throws Exception {
//	setViewSelector("release/features/simpleEditableList/simpleInmemoryEditableList");

//	list = new EditableBeanListWidget(new DemoEditableRowHandler(), Friend.class);

////	editableRows = 
////	new BeanFormListWidget(
////	new DemoEditableRowHandler(), 
////	new MapFormListModel(friends),
////	Friend.class);

////	addWidget("editableList", editableRows);

//	addWidget("list", list);
//	}

//	public class DemoEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
//	private static final long serialVersionUID = 1L;

//	public Object getRowKey(Object row) {
//	return ((Friend) row).getId();
//	}

//	public void saveValidRow(FormRow editableRow) throws Exception {
//	Friend rowData = (Friend) ((BeanFormWidget)editableRow.getForm()).writeToBean(new Friend());
//	rowData.setId((Long) editableRow.getKey());
//	friends.put(editableRow.getKey(), rowData);

//	editableRow.close();
//	}

//	public void deleteRow(Object key) throws Exception {
//	friends.remove(key);
//	}

//	public void addValidRow(FormWidget addForm) throws Exception {
//	lastId = Long.valueOf(lastId.longValue() + 1);
//	Friend newFriend = (Friend) ((BeanFormWidget)addForm).writeToBean(new Friend());
//	newFriend.setId(lastId);
//	friends.put(lastId, newFriend);
//	}

//	public void initFormRow(FormRow editableRow, Object row) throws Exception {
//	editableRow.close();

//	BeanFormWidget rowForm = (BeanFormWidget)editableRow.getForm();

//	addCommonFormFields(rowForm);

//	FormListUtil.addEditSaveButtonToRowForm("#", editableRows, rowForm, getRowKey(row));
//	FormListUtil.addDeleteButtonToRowForm("#", editableRows, rowForm, getRowKey(row));

//	rowForm.readFromBean(row);
//	}

//	public void initAddForm(FormWidget addForm) throws Exception {
//	addCommonFormFields(addForm);
//	FormListUtil.addAddButtonToAddForm("#", editableRows, addForm);
//	}

//	private void addCommonFormFields(FormWidget form) throws Exception {
//	form.addElement("forename", "#String field", new TextControl(), new StringData(), true);
//	form.addElement("surname", "#Long field", new TextControl(), new StringData(), true);
//	form.addElement("sex", "#Boolean field", new TextControl(), new StringData(), true);
//	}
//	}

	public static class Friend implements Serializable {
		private static final long serialVersionUID = 1L;
		private Long id;
		private String sex;
		private String forename;
		private String surname;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getForename() {
			return forename;
		}
		public void setForename(String forename) {
			this.forename = forename;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getSurname() {
			return surname;
		}
		public void setSurname(String surname) {
			this.surname = surname;
		}
		
		public void edit(Friend f) {
			setSurname(f.surname);
			setForename(f.forename);
			setSex(f.sex);
		}
	}

//	private class DataProvider extends MemoryBasedListDataProvider {
//	private static final long serialVersionUID = 1L;
//	protected DataProvider() {
//	super(Friend.class);
//	}
//	public List loadData() throws Exception {
//	return ;
//	}
//	}
}

