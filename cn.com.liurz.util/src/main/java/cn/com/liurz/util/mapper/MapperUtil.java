package cn.com.liurz.util.mapper;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * dozer工具类
 */
@Component
public class MapperUtil {
    Logger logger = LoggerFactory.getLogger(MapperUtil.class);
    @Autowired
    private Mapper dozer;

    public MapperUtil() {
    }

    public <T> T map(Object source, Class<T> destinationClass) {
        return this.dozer.map(source, destinationClass);
    }

    public <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList();
        Iterator var4 = sourceList.iterator();

        while (var4.hasNext()) {
            Object sourceObject = var4.next();
            T destinationObject = this.dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }

        return destinationList;
    }

    public void copy(Object source, Object destinationObject) {
        this.dozer.map(source, destinationObject);
    }
}
