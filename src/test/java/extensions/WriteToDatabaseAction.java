package extensions;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.PostServeAction;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

public class WriteToDatabaseAction extends PostServeAction {

    @Override
    public String getName() {
        return "write-to-database";
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {

        System.out.println("Writing to database " + parameters.getString("dbName"));
        System.out.println("--------------------------------------");
        System.out.printf("Match: %s, time elapsed: %s ms\n", serveEvent.getWasMatched(), serveEvent.getTiming().getTotalTime());
    }
}
