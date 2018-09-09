package com.revolut.account.mapper;

public interface EntityMapper<T1, T2> {

	T2 transferTo(T1 t1);
}
