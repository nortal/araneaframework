/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package org.araneaframework.example.main.web.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.GeneralMO;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;


public class SimpleSubBeanListWidget extends TemplateBaseWidget {
	protected static final Logger log = Logger.getLogger(SimpleSubBeanListWidget.class);
	
	private ListWidget list;
	
	protected void init() throws Exception {
		super.init();
		
		setViewSelector("list/subBeanList");
		
		this.list = initList();
		addWidget("list", this.list);
	}
	
	protected ListWidget initList() throws Exception {
		ListWidget list = new BeanListWidget(ContactMO.class);
		list.setDataProvider(new DataProvider());
		list.setOrderableByDefault(true);
		list.addField("id", "#Id", false);
		list.addField("name.firstname", "#First name").like();
		list.addField("name.lastname", "#Last name").like();
		list.addField("address.country", "#Country").like();
		list.addField("address.city", "#City").like();
		list.addField("dummy", null);
		return list;
	}
	
	private class DataProvider extends MemoryBasedListDataProvider {
		protected DataProvider() {
			super(ContactMO.class);
		}
		public List loadData() throws Exception {
			List contacts = new ArrayList(3);
			contacts.add(contact("Alice", "", "Wonderland", ""));
			contacts.add(contact("Chuck", "Norris", "USA", "Texas"));
			contacts.add(contact("Gudmund", "Edmundsdottir", "Iceland", ""));
			return contacts;
		}
		
		private long lastId = 0; 
		private ContactMO contact(String firstname, String lastname, String country, String city) {
			ContactMO contact = new ContactMO();
			contact.setId(new Long(++lastId));
			contact.setName(name(firstname, lastname));
			contact.setAddress(address(country, city));
			return contact;
		}
		private NameMO name(String firstname, String lastname) {
			NameMO name = new NameMO();
			name.setFirstname(firstname);
			name.setLastname(lastname);
			return name;
		}
		private AddressMO address(String country, String city) {
			AddressMO address = new AddressMO();
			address.setCountry(country);
			address.setCity(city);
			return address;
		}
	}
	
	public static class ContactMO implements GeneralMO {
		private Long id;
		private NameMO name;
		private AddressMO address;
		public AddressMO getAddress() {
			return address;
		}
		public void setAddress(AddressMO address) {
			this.address = address;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public NameMO getName() {
			return name;
		}
		public void setName(NameMO name) {
			this.name = name;
		}
		
	}
	
	public static class NameMO implements Serializable {
		private String firstname;
		private String lastname;
		public String getFirstname() {
			return firstname;
		}
		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
		public String getLastname() {
			return lastname;
		}
		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
	}
	
	public static class AddressMO implements Serializable {
		private String country;
		private String city;
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
	}
}
