
requirejs.config({
	paths: {
		jquery: "js/vendor/jquery-1.11.2.min",
		bootstrap: "node_modules/bootstrap/dist/js/bootstrap.bundle",
		mustache: "node_modules/mustache/mustache",
		multiselect: "node_modules/bootstrap-select-v4/dist/js/bootstrap-select"
    },
    map: {
        // '*' means all modules that define the specified module will get the corresponding module
        // Some modules, for some reason, say require("jQuery") instead of require("jquery")
        "*": { "jQuery": "jquery"}
    },
    shim: {
    	'jquery': {
    		exports: '$'
    	},
    	'bootstrap': {
    		deps: ['jquery']
    	},
    	'multiselect': {
    		deps: ['jquery']
    	}
    }
});

// Start loading the main app file. Put all of
// your application logic in there.
require(['js/main']);
