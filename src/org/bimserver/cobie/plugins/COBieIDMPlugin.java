package org.bimserver.cobie.plugins;

import java.io.File;
import java.util.ArrayList;

import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Package;
import org.bimserver.models.store.ObjectDefinition;
import org.bimserver.plugins.PluginException;
import org.bimserver.plugins.PluginManager;
import org.bimserver.plugins.objectidms.ObjectIDM;
import org.bimserver.plugins.objectidms.ObjectIDMPlugin;
import org.bimserver.utils.CollectionUtils;

public class COBieIDMPlugin implements ObjectIDMPlugin
{
	private static final String FILE_BASED_OBJECT_IDM_PLUGIN_NAME = "FileBasedObjectIDMPlugin";
	private boolean initialized;
	private COBieIDM cobieObjectIDM;
	private String ignoreFilePath="lib/COBieIgnore.xml";
	private File ignoreFile;
	@Override
	public void init(PluginManager pluginManager) throws PluginException
	{
		// TODO Auto-generated method stub
		ignoreFile = ConfigUtil.prepareSerializerConfigFile(pluginManager, "COBieIDM", this,ignoreFilePath);
		ObjectIDMPlugin fbPlugin = null;
		ArrayList<ObjectIDMPlugin> idmplugins = (ArrayList<ObjectIDMPlugin>) pluginManager.getAllObjectIDMPlugins(true);
		for(ObjectIDMPlugin plugin: idmplugins)
		{
			if (plugin.getDefaultName()==FILE_BASED_OBJECT_IDM_PLUGIN_NAME)
				fbPlugin = plugin;
		}
		cobieObjectIDM = new COBieIDM(ignoreFile,CollectionUtils.singleSet(Ifc2x3tc1Package.eINSTANCE), pluginManager.getPluginContext(this),fbPlugin);
		initialized = true;
		
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return "Ignores entities based on a list of Ifc entity types.";
	}

	@Override
	public String getVersion()
	{
		// TODO Auto-generated method stub
		return "1.0";
	}

	@Override
	public boolean isInitialized()
	{
		// TODO Auto-generated method stub
		return initialized;
	}

	@Override
	public ObjectIDM getObjectIDM()
	{
		// TODO Auto-generated method stub
		return cobieObjectIDM;
	}

	@Override
	public String getDefaultName()
	{
		// TODO Auto-generated method stub
		return "COBieIDMPlugin";
	}

	/* (non-Javadoc)
	 * @see org.bimserver.plugins.Plugin#getSettingsDefinition()
	 */
	@Override
	public ObjectDefinition getSettingsDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

}
