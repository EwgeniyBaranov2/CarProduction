package by.baranov.runner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate>{
	private final static String FORMAT_DATE_OUTPUT = "dd/MM/yyyy";
	
	@Override
	public String marshal(LocalDate localDate) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_OUTPUT);
		return localDate.format(formatter).toString();
	}

	@Override
	public LocalDate unmarshal(String stringDate) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_OUTPUT);
		return LocalDate.parse(stringDate, formatter);		
	}

}
