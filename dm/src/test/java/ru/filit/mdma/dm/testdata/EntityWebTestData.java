package ru.filit.mdma.dm.testdata;

public class EntityWebTestData {

  public static final String jsonClient1 = "[{\"id\":\"95471\",\"lastname\":\"Ворошилов\","
      + "\"firstname\":\"Клим\",\"patronymic\":\"Ефимович\",\"birthDate\":\"1900-04-28\","
      + "\"passportSeries\":\"6215\",\"passportNumber\":\"352617\",\"inn\":\"773626104512\","
      + "\"address\":\" г.Москва, ул. Ленина, дом 1\"}]";

  public static final String jsonContact1 = "[{\"id\":\"38523\",\"clientId\":\"95471\","
      + "\"type\":\"PHONE\",\"value\":\"+79161234567\",\"shortcut\":\"4567\"},{\"id\":\"63781\","
      + "\"clientId\":\"95471\",\"type\":\"EMAIL\",\"value\":\"voroshilovke@mail.ru\","
      + "\"shortcut\":\"e@mail.ru\"}]";

  public static final String jsonAccount1 = "[{\"number\":\"40817810452010063617\","
      + "\"clientId\":\"95471\",\"type\":\"PAYMENT\",\"currency\":\"RUR\",\"status\":\"CLOSED\","
      + "\"openDate\":\"2015-05-15\",\"closeDate\":\"2020-02-22\",\"shortcut\":\"3617\"},"
      + "{\"number\":\"40817810853110005823\",\"clientId\":\"95471\",\"type\":\"OVERDRAFT\","
      + "\"currency\":\"RUR\",\"status\":\"ACTIVE\",\"openDate\":\"2018-11-16\","
      + "\"deferment\":\"20\",\"shortcut\":\"5823\"}]";
}