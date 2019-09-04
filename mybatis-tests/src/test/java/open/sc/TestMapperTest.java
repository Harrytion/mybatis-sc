package open.sc;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import net.bytebuddy.ByteBuddy;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestMapperTest {

    private SqlSessionFactory sqlSessionFactory;
   // @Before
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

    @Test
    public void testByte(){

        String sql ="select * from user u left join test t on t.id=u.id left join (select count(*), t" +
                "id from test2 ) t1 on tid=t.id where username in ('1',2) and name='fix'";

        SQLStatementParser parser = new MySqlStatementParser(sql);

        SQLStatement sqlStatement = parser.parseStatement();

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();

        sqlStatement.accept(visitor);

        String dbType = visitor.getDbType();

        List<TableStat.Condition> conditions = visitor.getConditions();
        for (TableStat.Condition condition : conditions){
            TableStat.Column column = condition.getColumn();
            String operator = condition.getOperator();
            List<Object> values = condition.getValues();
            System.out.println(column+" "+operator+" "+values);
        }
        System.out.println("dbType:"+dbType);
        Map<TableStat.Name, TableStat> tables = visitor.getTables();

        for (TableStat.Name name : tables.keySet()){
            System.out.println(name.getName());
        }


    }
}

