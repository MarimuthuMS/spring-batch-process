package com.springbatch.demo.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String empId;
	private String name;
	private Date dob;
	private Double rating;
	private boolean isHighPerfomer;
	private boolean isSeniorEmployee;
	
	public Employee(Integer id, String name, Date dob, Double rating,String empId) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.rating = rating;
	}
	public Employee() {
		
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	
	
	public boolean isHighPerfomer() {
		return isHighPerfomer;
	}
	public void setHighPerfomer(boolean isHighPerfomer) {
		this.isHighPerfomer = isHighPerfomer;
	}
	public boolean isSeniorEmployee() {
		return isSeniorEmployee;
	}
	public void setSeniorEmployee(boolean isSeniorEmployee) {
		this.isSeniorEmployee = isSeniorEmployee;
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((empId == null) ? 0 : empId.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Employee other = (Employee) obj;
	    if (empId == null) {
	        if (other.empId != null)
	            return false;
	    } else if (!empId.equals(other.empId))
	        return false;
	    return true;
	}
	
	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", name=" + name + "]";
	}
}
