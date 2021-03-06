package ru.filit.mdma.dm.testdata;

public class EntityWebTestData {

  public static final String jsonClient1 = "[{\"id\":\"95471\",\"lastname\":\"Ворошилов\","
      + "\"firstname\":\"Клим\",\"patronymic\":\"Ефимович\",\"birthDate\":\"1900-04-28\","
      + "\"passportSeries\":\"6215\",\"passportNumber\":\"352617\",\"inn\":\"773626104512\","
      + "\"address\":\" г.Москва, ул. Ленина, дом 1\"}]";

  public static final String jsonContact1 = "[{\"id\":\"34028\",\"clientId\":\"80302\","
      + "\"type\":\"PHONE\",\"value\":\"+79079256329\",\"shortcut\":\"6329\"},{\"id\":\"42906\","
      + "\"clientId\":\"80302\",\"type\":\"PHONE\",\"value\":\"+79633505208\","
      + "\"shortcut\":\"5208\"},{\"id\":\"45152\",\"clientId\":\"80302\",\"type\":\"EMAIL\","
      + "\"value\":\"anisimov.ad@mail.ru\",\"shortcut\":\"d@mail.ru\"}]";

  public static final String jsonAccount1 = "[{\"number\":\"40817810220135711049\","
      + "\"clientId\":\"74914\",\"type\":\"PAYMENT\",\"currency\":\"RUR\",\"status\":\"CLOSED\","
      + "\"openDate\":\"2021-01-15\",\"closeDate\":\"2021-06-09\",\"shortcut\":\"1049\"},"
      + "{\"number\":\"40817810110167668404\",\"clientId\":\"74914\",\"type\":\"PAYMENT\","
      + "\"currency\":\"RUR\",\"status\":\"ACTIVE\",\"openDate\":\"2021-08-11\","
      + "\"shortcut\":\"8404\"}]";

  public static final String jsonOperationList = "[{\"type\":\"RECEIPT\","
      + "\"accountNumber\":\"40817810670114037905\",\"operDate\":\"2021-12-10T00:16:40\","
      + "\"amount\":\"10000.00\",\"description\":\"VSP 5318 MOSKVA RUS\"},{\"type\":\"EXPENSE\","
      + "\"accountNumber\":\"40817810670114037905\",\"operDate\":\"2021-12-10T00:00:00\","
      + "\"amount\":\"5645.20\",\"description\":\"Амбарчик. Оплата\"},{\"type\":\"RECEIPT\","
      + "\"accountNumber\":\"40817810670114037905\",\"operDate\":\"2021-11-27T02:03:20\","
      + "\"amount\":\"10000.00\",\"description\":\"VSP 5318 MOSKVA RUS\"}]";

  public static final String jsonBalanceAmount = "{\"balanceAmount\":\"-5648.00\"}";

  public static final String jsonUpdateContactSave = "{\"id\":\"38523\",\"clientId\":\"95471\","
      + "\"type\":\"PHONE\",\"value\":\"+79805243600\",\"shortcut\":\"3600\"}";

  public static final String jsonClientGetById = "{\"id\":\"95471\",\"lastname\":\"Ворошилов\","
      + "\"firstname\":\"Клим\",\"patronymic\":\"Ефимович\",\"birthDate\":\"1900-04-28\","
      + "\"passportSeries\":\"6215\",\"passportNumber\":\"352617\",\"inn\":\"773626104512\","
      + "\"address\":\" г.Москва, ул. Ленина, дом 1\"}";

  public static final String jsonLoanPaymentAmount = "{\"amount\":\"405.73\"}";

  public static final String jsonAccessList = "[{\"entity\":\"client\",\"property\":\"id\"},"
      + "{\"entity\":\"client\",\"property\":\"lastname\"}]";
}