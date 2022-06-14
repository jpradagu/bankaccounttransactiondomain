package com.nttdata.bootcamp.transactiondomain.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CustomerPersonal {
	private String id;
	private String dni;
	private String name;
	private String lastname;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date datebirth;
	private String phone;
	private String email;
	private String address;
}
