package com.itrustcambodia.plugin.query.plugin;

import com.itrustcambodia.pluggable.core.AbstractPlugin;
import com.itrustcambodia.pluggable.core.AbstractWebApplication;
import com.itrustcambodia.pluggable.migration.AbstractPluginMigrator;
import com.itrustcambodia.pluggable.page.PluginSettingPage;
import com.itrustcambodia.pluggable.page.WebPage;
import com.itrustcambodia.plugin.query.page.SQLQueryPage;
import com.itrustcambodia.plugin.query.page.SettingPage;

public class QueryPlugin extends AbstractPlugin {

    public static final String ID = "com.itrustcambodia.plugin.query";

    @Override
    public String getName() {
        return "Query Plugin";
    }

    @Override
    public boolean activate(AbstractWebApplication application) {
        return true;
    }

    @Override
    public void initialize(AbstractWebApplication application) {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public String getIdentity() {
        return ID;
    }

    @Override
    public Class<? extends PluginSettingPage> getSettingPage() {
        return SettingPage.class;
    }

    @Override
    public Class<? extends WebPage> getDashboardPage() {
        return SQLQueryPage.class;
    }

    @Override
    public Class<? extends AbstractPluginMigrator> getMigrator() {
        return QueryMigrator.class;
    }

    @Override
    public String[] getPackages() {
        return new String[] { "com.itrustcambodia.plugin.query" };
    }

}
