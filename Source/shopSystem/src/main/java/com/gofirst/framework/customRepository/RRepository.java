
package com.gofirst.framework.customRepository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@SuppressWarnings("rawtypes")
@NoRepositoryBean
public interface RRepository extends Repository
{

    public abstract Object findById(Long long1);

    public abstract boolean exists(Serializable serializable);
}
