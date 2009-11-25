/*
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
 */

package org.araneaframework.example.main.release.features;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExampleData {

  public static final String males[] = { "Adar", "Arel", "Arhe", "Daniel-Bhekiwe", "Dimon", "Georg-Goswin", "Gomer",
      "Greito", "Hasgeir-Egert", "Indre", "Indy", "Ino", "Jamo", "Japheth", "Jeonel", "Jorke Patrick", "Kellven",
      "Kendris", "Kenomar", "Kösta", "Luukas Aeg", "Matrix", "Norton", "Oleander", "Radu", "Rato", "Relon", "Rhening",
      "Riko-Givy", "Ruud", "Signor", "Taigo Juunior", "Tristan Timothy", "Uko Hiid", "Örö Tormi" };

  public static final String females[] = { "Aaria Lee", "Berlyka", "Bretie", "Cayro-Lyza", "Cameron", "Carlyne",
      "Carmelyta", "Chantell", "Darelyan", "Emmelgelina", "Genivive", "Grittel", "Gynethy", "Hailely", "Haldja",
      "Hertrudis", "Isis", "Jonete", "Kaleria", "Karetti", "Keira", "Kirteli", "Kärg", "Lomely", "Lorelai",
      "Madara Kuld", "Marimar", "Mirjam Sürr", "Nia", "Patrina", "Pipi Triinu", "Priti", "Randelyn", "Reneeta",
      "Rinell", "Rhe", "Sarmika-Hell", "Sevinc", "Sibell", "Sisandra", "Viive-Virhiinya", "Virbi Kleer" };

  public static final String fungi[] = { "Männi-kivipuravik", "Männiliimik", "Kobarkõrges", "Rõngasvöödik",
      "Kollanutt", "Narmasnutt", "Kastan-narmasnutt", "Kuhik-narmasnutt", "Lakkrupik", "Kamperriisikas", "Sooriisikas",
      "Näsariisikas", "Tõmmuriisikas", "Männiriisikas", "Tavariisikas", "Hallipiimane", "Kasepuravik", "Suursirmik",
      "Sametvahelik", "Tavavahelik", "Leekmampel", "Soomusmampel", "Kitsemampel", "Kasepilvik", "Soopilvik",
      "Kuld-puiduheinik", "Sapipuravik", "Pruunsamet", "Koorikkulised", "Kasepehik", "Majamädik", "Hõbelehisus",
      "Tuletael", "Kännupess", "Jänesvaabik", "Mustpässik", "Vahtratarjak", "Juurepruunik", "Must", "Männitaelik",
      "Täpptael", "Haavataelik", "Kasekäsnak", "Karvane", "Lehternahkes", "Kuusekõbjuk" };

  // attendee attends the appointment ;)
  public static class Attendee implements Serializable {

    private Long id;

    private String name;

    private boolean preoccupied;

    public Attendee(String name, boolean preoccupied) {
      this.name = name;
      this.preoccupied = preoccupied;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
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

    @Override
    public String toString() {
      return this.name;
    }
  }

  public static List<Attendee> createAttendees() {
    List<Attendee> result = new ArrayList<Attendee>(6);
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

    private Long id;

    private String name;

    private String location;

    private boolean occupied;

    public Room(Long id, String name, String location, boolean occupied) {
      this.id = id;
      this.name = name;
      this.location = location;
      this.occupied = occupied;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
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

    public String getLabel() {
      return this.name + ", " + this.location;
    }
  }

  public static class Client implements Serializable {

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
      setCountry(f.country);
      setSurname(f.surname);
      setForename(f.forename);
      setSex(f.sex);
      setCountry(f.country);
    }
  }

  public static List<Room> createRooms() {
    List<Room> result = new ArrayList<Room>(5);
    result.add(new Room(1L, "seamless.rooms.atrium", "Tartu", false));
    result.add(new Room(2L, "seamless.rooms.olympic", "Tallinn", false));
    result.add(new Room(3L, "seamless.rooms.colosseum", "Rome", false));
    result.add(new Room(4L, "seamless.rooms.rld", "Zona Norte", true));
    result.add(new Room(5L, "seamless.rooms.warroom", "Washington", false));
    return result;
  }

}
