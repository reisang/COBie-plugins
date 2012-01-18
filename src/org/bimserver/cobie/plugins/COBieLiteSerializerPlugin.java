package org.bimserver.cobie.plugins;
/******************************************************************************
 * Copyright (C) 2011  ERDC
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
import java.io.File;

import org.bimserver.plugins.PluginException;
import org.bimserver.plugins.PluginManager;
import org.bimserver.plugins.serializers.EmfSerializer;
import org.bimserver.plugins.serializers.SerializerPlugin;

public class COBieLiteSerializerPlugin implements SerializerPlugin{
	private boolean initialized = false;
	//private File configurationFile;
	
	@Override
	public String getDescription() {
		return "XML based on a schema derived from the COBie spreadsheet columns.";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public void init(PluginManager pluginManager) throws PluginException {
		pluginManager.requireSchemaDefinition();
	//	configurationFile = ConfigUtil.prepareConfigFile(pluginManager, "COBieLiteSerializer", this);
		initialized = true;
	}
	
	@Override
	public EmfSerializer createSerializer() {
		return new COBieLiteSerializer();
	}
	
	@Override
	public String getDefaultSerializerName() {
		//return "COBIE";
		return "COBieLite(xml)";
	}

	///////////////////////////////
	///need to fix these content and extension functions.
	///had to use html to get it to work
	@Override
	public String getDefaultContentType() {
		//return "appliction/COBIE";
		return "application/XML";
	}

	@Override
	public String getDefaultExtension() {
		//return "xml";//Change this to proper extension
		return "xml";
	}
 /////////////////////////////////////////////////
	@Override
	public boolean isInitialized() {
		return initialized;
	}
}