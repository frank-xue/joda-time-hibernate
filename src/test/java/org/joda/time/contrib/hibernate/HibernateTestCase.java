/*
 *  Copyright 2001-2012 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.contrib.hibernate;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.util.EnumSet;

public abstract class HibernateTestCase extends TestCase
{
    private SessionFactory factory;
    protected Metadata metadata;

    protected SessionFactory getSessionFactory()
    {
        if (this.factory == null)
        {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySetting("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver")
                    .applySetting("hibernate.connection.url", "jdbc:hsqldb:mem:hbmtest" + getClass().getName())
                    .applySetting("hibernate.dialect", HSQLDialect.class.getName())
                    .applySetting("hibernate.show_sql", "true")
                    .build();

            metadata =  getMetadata(registry);

            this.factory = metadata.buildSessionFactory();
            new SchemaExport().create(EnumSet.of(TargetType.DATABASE, TargetType.STDOUT), metadata);
        }
        return this.factory;
    }

    protected void tearDown() throws Exception
    {
        new SchemaExport().drop(EnumSet.of(TargetType.DATABASE, TargetType.STDOUT), metadata);
    }

    protected abstract Metadata getMetadata(StandardServiceRegistry registry);
}
