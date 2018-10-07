package de.kofk.routingcomp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.kofk.routingcomp.service.GoogleServiceConnectorTest;
import de.kofk.routingcomp.service.ORSServiceConnectorTest;
import de.kofk.routingcomp.service.DummyServiceConnectorTest;

@RunWith(Suite.class)
@SuiteClasses({ GoogleServiceConnectorTest.class, ORSServiceConnectorTest.class, DummyServiceConnectorTest.class})
public class AllTests {

}
