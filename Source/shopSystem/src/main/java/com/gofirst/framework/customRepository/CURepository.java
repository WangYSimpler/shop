package com.gofirst.framework.customRepository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@SuppressWarnings("rawtypes")
@NoRepositoryBean
public interface CURepository extends Repository
{
    public abstract Object save(Object obj);
}
