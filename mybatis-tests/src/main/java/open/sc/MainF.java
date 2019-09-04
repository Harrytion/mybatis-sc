package open.sc;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.TargetType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MainF {
    public static void main(String[] args) {
//        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
//        InputStream inputStream = MainF.class.getResourceAsStream("/mybatis-config.xml");
//        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
//        test1(sqlSessionFactory);
        new ByteBuddy(ClassFileVersion.JAVA_V8).subclass(Object.class);
    }

    public static void test1(SqlSessionFactory sqlSessionFactory){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
        int count = mapper.selectCount();
        System.out.println(count);
    }
}

