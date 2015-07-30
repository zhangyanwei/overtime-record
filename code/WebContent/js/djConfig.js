
var dojoVersion = "1.8.3", ctdVersion = "1.0";

djConfig = {
    isDebug: false,
    // the default configuration for a browser; this will be modified by other environments
    hasCache:{
        "host-browser":1,
        "dom":1,
        "dojo-amd-factory-scan":1,
        "dojo-loader":1,
        "dojo-has-api":1,
        "dojo-inject-api":1,
        "dojo-timeout-api":1,
        "dojo-trace-api":1,
        "dojo-log-api":1,
        "dojo-dom-ready-api":1,
        "dojo-publish-privates":1,
        "dojo-config-api":1,
        "dojo-sniff":1,
        "dojo-sync-loader":1,
        "dojo-test-sniff":1,
        "config-tlmSiblingOfDojo":1
    },
    packages:[{
        // note: like v1.6-, this bootstrap computes baseUrl to be the dojo directory
        name:'dojo',
        location:'.'
    },{
        name:'tests',
        location:'./tests'
    },{
        name:'dijit',
        location:'../dijit'
    },{
        name:'build',
        location:'../util/build'
    },{
        name:'doh',
        location:'../util/doh'
    },{
        name:'dojox',
        location:'../dojox'
    },{
        name:'ctd',
        //location:'../../../jslib/ctd'
        location:'../../../ctd/1.0'
    },{
        name:'jquery',
        //location:'../../../jslib/jquery'
        location:'../../../jquery/1.9.1'
    }],
    trace:{
        // these are listed so it's simple to turn them on/off while debugging loading
        "loader-inject":0,
        "loader-define":0,
        "loader-exec-module":0,
        "loader-run-factory":0,
        "loader-finish-exec":0,
        "loader-define-module":0,
        "loader-circular-dependency":0
    },
    async:0
};