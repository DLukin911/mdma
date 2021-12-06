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

  public static final String jsonOperationList = "[{\"type\":\"RECEIPT\","
      + "\"accountNumber\":\"40817810853110005823\",\"operDate\":\"2021-10-19T12:36:13\","
      + "\"amount\":\"5000.00\",\"description\":\"VSP 5311 MOSKVA RUS\"},{\"type\":\"EXPENSE\","
      + "\"accountNumber\":\"40817810853110005823\",\"operDate\":\"2021-10-12T18:15:12\","
      + "\"amount\":\"4926.59\",\"description\":\"YM OZON 1 GOROD MOSKVA RUS\"},"
      + "{\"type\":\"EXPENSE\",\"accountNumber\":\"40817810853110005823\","
      + "\"operDate\":\"2021-09-09T15:40:11\",\"amount\":\"215.00\","
      + "\"description\":\"МУП Водоканал. Оплата услуг\"}]";

  public static final String jsonBalanceAmount = "{\"balanceAmount\":\"60500.00\"}";
}