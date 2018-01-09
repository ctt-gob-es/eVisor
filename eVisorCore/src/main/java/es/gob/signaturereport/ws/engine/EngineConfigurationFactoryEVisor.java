package es.gob.signaturereport.ws.engine;

import javax.servlet.ServletConfig;

import org.apache.axis.AxisProperties;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.EngineConfigurationFactory;
import org.apache.axis.configuration.EngineConfigurationFactoryFinder;
import org.apache.axis.configuration.FileProvider;

import es.gob.signaturereport.utils.UtilsJBoss;

public class EngineConfigurationFactoryEVisor implements
		EngineConfigurationFactory {
	
	/**
	 * 
	 */
	public static final String OPTION_CLIENT_CONFIG_FILE = "axis.ClientConfigFile";

	/**
	 * Constant attribute that represents the filename for the client config file
	 * for specific the AXIS transports configuration.
	 */
	public static final String AXIS_CLIENT_CONFIG_FILENAME = "client-config.wsdd";

	/**
	 * Constant attribute that represents the filename for the server config file
	 * for WSDD.
	 */
	public static final String AXIS_CLIENT_SERVER_FILENAME = "server-config.wsdd";

	/**
	 * Constant attribute that represents the absolute path to the Client Config Axis WSDD in
	 * the configuration JBoss directory.
	 */
	public static final String ABS_PATH_TO_AXIS_CLIENT_CONFIG = UtilsJBoss.createAbsolutePath(UtilsJBoss.getJBossServerConfigDir(), AXIS_CLIENT_CONFIG_FILENAME);

	/**
	 * Constant attribute that represents the absolute path to the Client Config Axis WSDD in
	 * the configuration JBoss directory.
	 */
	public static final String ABS_PATH_TO_AXIS_SERVER_CONFIG = UtilsJBoss.createAbsolutePath(UtilsJBoss.getJBossServerConfigDir(), AXIS_CLIENT_SERVER_FILENAME);
	
	protected String clientConfigFile;

	/**
	 * Method used by {@link EngineConfigurationFactoryFinder} to initialize this class.
	 * @param param In theory, it is a {@link ServletConfig} parameter.
	 * @return A new instance of the {@link EngineConfigurationFactoryAfirma}.
	 */
	public static EngineConfigurationFactory newFactory(Object param) {
		return new EngineConfigurationFactoryEVisor();
	}

	/**
	 * Constructor method for the class EngineConfigurationFactoryAfirma.java.
	 */
	public EngineConfigurationFactoryEVisor() {
		super();
		
		clientConfigFile =
	            AxisProperties.getProperty(OPTION_CLIENT_CONFIG_FILE,
	            		AXIS_CLIENT_CONFIG_FILENAME);
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.EngineConfigurationFactory#getClientEngineConfig()
	 */
	@Override
	public final EngineConfiguration getClientEngineConfig() {
		//return new FileProvider(ABS_PATH_TO_AXIS_CLIENT_CONFIG);
		
//		BasicClientConfig cfg = new BasicClientConfig();
//        cfg.deployTransport("handlerClass", new RPCProvider());
//        return cfg;
		
		 return new FileProvider(clientConfigFile);
	}

	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.EngineConfigurationFactory#getServerEngineConfig()
	 */
	@Override
	public final EngineConfiguration getServerEngineConfig() {
		return new FileProvider(ABS_PATH_TO_AXIS_SERVER_CONFIG);
	}

}
