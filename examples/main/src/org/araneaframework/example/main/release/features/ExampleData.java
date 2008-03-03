package org.araneaframework.example.main.release.features;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExampleData {

	public static final String males[] = { "Adar","Arel","Arhe","Daniel-Bhekiwe","Dimon","Georg-Goswin","Gomer","Greito","Hasgeir-Egert","Indre","Indy","Ino","Jamo","Japheth","Jeonel","Jorke Patrick","Kellven","Kendris","Kenomar","Kösta","Luukas Aeg","Matrix","Norton","Oleander","Radu","Rato","Relon","Rhening","Riko-Givy","Ruud","Signor","Taigo Juunior","Tristan Timothy","Uko Hiid","Örö Tormi"};
	public static final String females[] = {"Aaria Lee","Berlyka","Bretie","Cayro-Lyza","Cameron","Carlyne","Carmelyta","Chantell","Darelyan","Emmelgelina","Genivive","Grittel","Gynethy","Hailely","Haldja","Hertrudis","Isis","Jonete","Kaleria","Karetti","Keira","Kirteli","Kärg","Lomely","Lorelai","Madara Kuld","Marimar","Mirjam Sürr","Nia","Patrina","Pipi Triinu","Priti","Randelyn","Reneeta","Rinell","Rhe","Sarmika-Hell","Sevinc","Sibell","Sisandra","Viive-Virhiinya","Virbi Kleer"};
	public static final String fungi[] = {
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
	

	// attendee attends the appointment ;)
	public static class Attendee implements Serializable {
		private String name;
		private boolean preoccupied;
		
		public Attendee(String name, boolean preoccupied) {
			this.name = name;
			this.preoccupied = preoccupied;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isPreoccupied() {
			return preoccupied;
		}
		public void setPreoccupied(boolean occupied) {
			this.preoccupied = occupied;
		}
	}
	
	public static List createAttendees() {
		List result = new ArrayList();
		result.add(new Attendee("Aileen Wuornos", false));
		result.add(new Attendee("David Berkowitz", false));
		result.add(new Attendee("Elizabeth Bathory", false));
		result.add(new Attendee("Karla Homolka", false));
		result.add(new Attendee("Kitty Genovese", true));
		result.add(new Attendee("Theo Durrant", false));

		return result;
	}

	// room is where the appointment takes place
	public static class Room implements Serializable {
		private String name;
		private String location;
		private boolean occupied;

		public Room(String name, String location, boolean occupied) {
			this.name = name;
			this.location = location;
			this.occupied = occupied;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public boolean isOccupied() {
			return occupied;
		}
		public void setOccupied(boolean occupied) {
			this.occupied = occupied;
		}
	}

	public static class Client implements Serializable {
		private static final long serialVersionUID = 1L;
		private Long id;
		private String sex;
		private String forename;
		private String surname;
		private String country;
		
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
		
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public void edit(Client f) {
			setSurname(f.surname);
			setForename(f.forename);
			setSex(f.sex);
			setCountry(f.country);
		}
	}

	public static List createRooms() {
		List result = new ArrayList();
		result.add(new Room("seamless.rooms.atrium", "Tartu", false));
		result.add(new Room("seamless.rooms.olympic", "Tallinn", false));
		result.add(new Room("seamless.rooms.colosseum", "Rome", false));
		result.add(new Room("seamless.rooms.rld", "Zona Norte", true));
		result.add(new Room("seamless.rooms.warroom", "Washington", false));

		return result;
	}
}
