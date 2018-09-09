package com.revolut.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.revolut.account.api.request.dto.MoneyTransferRequestDto;
import com.revolut.account.db.entity.MoneyTransferLog;

@Mapper
public interface MoneyTransferEntityMapper extends EntityMapper<MoneyTransferRequestDto, MoneyTransferLog> {

	MoneyTransferEntityMapper INSTANCE = Mappers.getMapper(MoneyTransferEntityMapper.class);

	@Mappings({ @Mapping(source = "fromAccountNumber", target = "fromAccountNumber"),
			@Mapping(source = "toAccountNumber", target = "toAccountNumber"),
			@Mapping(source = "amount", target = "amount"), @Mapping(source = "createdAt", target = "createdAt") })
	MoneyTransferLog transferTo(MoneyTransferRequestDto dto);
}
