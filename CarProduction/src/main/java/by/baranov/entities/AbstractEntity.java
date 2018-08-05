package by.baranov.entities;

import javax.xml.bind.annotation.XmlElement;

public abstract class AbstractEntity {
	
	private Long id;
	
	public AbstractEntity(){		
	}
	public AbstractEntity(Long id){ 
		this.id = id;
	}	
	@XmlElement(name = "id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	
}
