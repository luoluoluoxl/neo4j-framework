/*
 * Copyright (c) 2013-2020 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.server.foundation.context;

import com.graphaware.common.ping.StatsCollector;
import com.graphaware.runtime.GraphAwareRuntime;
import com.graphaware.runtime.RuntimeRegistry;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.server.NeoServer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class FoundationRootContextCreator implements RootContextCreator {

    @Override
    public AbstractApplicationContext createContext(NeoServer neoServer, StatsCollector statsCollector) {
        GenericApplicationContext parent = new GenericApplicationContext();
        parent.registerShutdownHook();
        parent.getBeanFactory().registerSingleton("database", neoServer.getDatabase().getGraph());
        parent.getBeanFactory().registerSingleton("procedures", neoServer.getDatabase().getGraph().getDependencyResolver().resolveDependency(Procedures.class));


        parent.getBeanFactory().registerSingleton("getStatsCollector", statsCollector);

        GraphAwareRuntime runtime = RuntimeRegistry.getRuntime(neoServer.getDatabase().getGraph());
        if (runtime != null) {
            runtime.waitUntilStarted();
            parent.getBeanFactory().registerSingleton("databaseWriter", runtime.getDatabaseWriter());
        }

        parent.refresh();

        return parent;
    }
}
