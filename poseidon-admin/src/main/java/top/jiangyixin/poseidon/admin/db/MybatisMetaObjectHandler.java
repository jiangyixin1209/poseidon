package top.jiangyixin.poseidon.admin.db;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author jiangyixin
 * @version 1.0
 * @date 2021/1/3
 */
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {
  @Override
  public void insertFill(MetaObject metaObject) {
    this.setFieldValByName("gmtCreate", LocalDateTime.now(), metaObject);
    this.setFieldValByName("gmtModified", LocalDateTime.now(), metaObject);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    this.setFieldValByName("gmtModified", LocalDateTime.now(), metaObject);
  }
}
