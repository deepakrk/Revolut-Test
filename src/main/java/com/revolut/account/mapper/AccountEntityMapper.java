package com.revolut.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.revolut.account.api.request.dto.AccountCreateRequestDto;
import com.revolut.account.db.entity.Account;

@Mapper
public interface AccountEntityMapper {

	AccountEntityMapper INSTANCE = Mappers.getMapper(AccountEntityMapper.class);

	@Mappings({ @Mapping(source = "fullName", target = "fullName"),
			@Mapping(source = "accountNumber", target = "accountNumber"),
			@Mapping(source = "depositAmount", target = "amount") })
	Account accountRequestDtoToEntity(AccountCreateRequestDto dto);
}
