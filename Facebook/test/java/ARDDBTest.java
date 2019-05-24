
import com.qmo.facebook.configurations.EDAConfigurations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by x218647 on 11/14/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EDAConfigurations.class, loader = AnnotationConfigContextLoader.class)
public class ARDDBTest {
    @Autowired
    private JdbcTemplate edaTestDataJdbcTemplate;

   // @Autowired
    //ScenarioXmlsService scenarioXmlsService;

    private final String loadARDDataQuery = "select PNR,TICKETNOS,PNRCREATEDATE,RELEASE FROM CDS_INPUT WHERE ScenarioName=#ScenarioName";

    private final String insertARDDataQuery = "insert into eda_scenario_info(SCENARIO_NAME,PNR_ID,TICKET_NOS,PNR_CREATE_DATE,RELEASE,CREATE_TIME_STAMP) values(?,?,?,?,?,?)";

    @Test
    public  void  testing() throws InterruptedException {
        try {

            //for(int i=0;i<10;i++){
               // Thread.sleep(4000);
                saveARDData123();
           // }
        } catch (org.springframework.jdbc.UncategorizedSQLException e) {
            e.printStackTrace();
        }

    }
/*
    //@Test
    public void getLatestDate() throws IOException {
        ScenarioInfoTO scenarioInfoTO = new ScenarioInfoTO();
        scenarioInfoTO.setPnr("35WZ6P");
        scenarioInfoTO.setTicketNos("5268536012561,5268536012561,5268536012563");
        scenarioInfoTO.setPnrCreateDate("10-NOV-16");
        scenarioInfoTO.setScenarioName(scenarioInfoTO.getPnr());
        scenarioInfoTO.setRelease("R1");
        scenarioXmlsService.loadCdmFeed(scenarioInfoTO);
    }*/

    @Transactional
    public Boolean saveARDData123(){
        return edaTestDataJdbcTemplate.execute(insertARDDataQuery,new PreparedStatementCallback<Boolean>(){
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                ps.setString(1,"RevIntMultipaxPaxwithEXTSaddedto1pax");
                ps.setString(2,"LFK8D9");
                ps.setString(3,"526-8550794103,526-8550794102,526-8550794101");
                ps.setString(4,"03-FEB-17");
                ps.setString(5,"R2");
                long time = new Date().getTime();
                ps.setTimestamp(6, new Timestamp(time));

                return ps.execute();

            }
        });
    }

    private static Timestamp getCurrentTimeStamp() {

        Date today = new Date();
        return new Timestamp(today.getTime());

    }


}



