package org.dante.springboot.thirdclient.vo.dante;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DanteUserVO {
	private Long id;
	private String account;
	private String name;
	private int age;
	private BigDecimal balance;
}
