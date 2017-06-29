package com.gofirst.framework.customRepository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@SuppressWarnings("rawtypes")
@NoRepositoryBean
public interface DRepository extends Repository
{
    public abstract void delete(Object obj);
}
