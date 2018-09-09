package com.revolut.account.db.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedNativeQueries({
		@NamedNativeQuery(name = "com.revolut.account.db.entity.Account.findByAccountNumber", query = "select * from account a where a.account_number = :accountNumber", resultClass = Account.class) })

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(columnNames = { "account_number" }))
public class Account {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Size(min = 12, max = 14)
	@Column(name = "account_number", nullable = false)
	private String accountNumber;

	@Min(0)
	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "default_currency_type", nullable = false)
	private String defaultCurrencyType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;

}
