package open.sc;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestMapperTest {

    private SqlSessionFactory sqlSessionFactory;
    @Before
    public void init() throws IOException {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File("G:\\jpro\\mybatis-sc\\mybatis-tests\\src\\main\\resources\\mybatis-config.xml")));
        sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
    }

    @Test
    public void testMybatis(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
        int count = mapper.selectCount();
        System.out.println(count);
    }


    @Test
    public void test1(){
        List<Integer> integers = Arrays.asList(1, 2, 5, 4, 3, 8, 9);
        System.out.println(integers);
        Collections.sort(integers,(i1,i2)->i2-i1);
        System.out.println(integers);
    }
}

