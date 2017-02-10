       
/**
 * A simple configuration editor
 *
 * @param parent The parent jQuery to append the config UI to
 * @param config The connection arguments.
 * @param updateCB Callback fired when configuration should be updated
 * @throws TypeError if parent or config is not provided.
 */
/*var demoConfigurator = function(parent, config, updateCB) {
    var _getCfg = function(view) {
        view = (view || $("table.config")).find("input");
        var ret = {};
        for (var idx = 0; idx < view.length; ++idx) {
            ret[$(view[idx]).attr("id")] = $(view[idx]).val();
        }
        return ret;
    };
    var _setCfg = function(config, view, caption) {
        config = config || {};
        caption = caption || "CAXL-release-2014.04.10787";
        view = view || $("table.config");
        if (view) {
            view.empty();
            view.append("<caption class='config'>" + caption + "</caption>");
            for (var p in config) {
                if (config.hasOwnProperty(p)) {
                view.append(
                    "<tr> \
                        <td class='config_prop'><label for='" + p + "'>" + p + ": </label></td> \
                        <td><input class='config' type='text' id='" + p + "'/></td> \
                    </tr>");
                jabberwerx.$("input#" + p, view).val(config[p]);
                }
            }
        }
    };

    if (!parent || !config) {
        throw new TypeError("newConfig must be given a parent and configuration");
    }

    parent.append("<div class='jabberwerx config'></div>");
    $("div.config", parent).append("<table class='config'></table>");
    $("div.config", parent).append("<div class='btnbar'></div>");

    _setCfg(config, $("table.config", parent));

    $("div.btnbar", parent).append("<button class='config-left' id='applyCfg'>Apply</button>");
    $("button#applyCfg").click(function(){
        if (updateCB) {
            var cfg = _getCfg($("table.config", parent));
            updateCB.call(this, cfg);
        }
    });
    $("div.btnbar", parent).append("<button class='config-right' id='defaultCfg'>Default</button>");
    $("button#defaultCfg", parent).click(function(){
        _setCfg(config, $("table.config", parent));
    });
    $("div.btnbar", parent).append("<button class='config-right' id='saveCfg'>Save</button>");
    $("button#saveCfg", parent).click(function(){
        var cfg = _getCfg($("table.config", parent));
        cfg = jabberwerx.util.serialize(cfg);
        localStorage.setItem("caxl-sampleclient-cfg", cfg);
    });
    $("div.btnbar", parent).append("<button class='config-right' id='loadCfg'>Load</button>");
    $("button#loadCfg", parent).click(function(){
        var cfg = localStorage.getItem("caxl-sampleclient-cfg");
        if (cfg) {
            cfg = $.parseJSON(cfg);
            _setCfg(cfg, $("table.config", parent));
        }
    });
}*/
var client;
var auth;

var roster;
var quickContacts;

var chat;
var muc ;
var room;
var tabbedView;

var demo_config = {
    //CAXL connection options
    connectArgs: {
        httpBindingURL: "https://im3.ciscowebex.com/http-bind",
        // The COnnect server domain
        domain: "cisco.com",
        // is SASL PLAIN allowed?
        unsecureAllowed: false,
        legacyAuth : true,
        // Additional CUP HA options
        // Is service discovery enabled on client
        serviceDiscoveryEnabled: false,
        // Default secondary binding url
        //httpBindingURL_secondary: "https://im5.ciscowebex.com/http-bind",
        bindingproxy : "https://im3.ciscowebex.com/http-bind",
        // Default bind retry period
        bindRetryCountdown: jabberwerx.cisco.cupha.DEFAULT_CONNECTION_TIMEOUT,
        logPersistence: true,
        serviceDiscoveryEnabled: false,
     // Additional CUP HA options
     // Is service discovery enabled on client
     serviceDiscoveryEnabled: false,
     maxGraphAge : 3000,
     appTag : "FirstContact-appv1",
    },
    //conference server used in this demo.
    //If left empty/null the demo will populate it with the  first conference server
    //returned by disco walk
    defaultConferenceServer: "conference.corp.jabber.com",
    //Login with a username@domain vs just a username
    atSymbolAllowed: false
};
jabberwerx.util.config.logPersistence = demo_config.connectArgs.logPersistence;
jabberwerx.util.setMaxGraphAge(demo_config.connectArgs.maxGraphAge);
jabberwerx._config.unsecureAllowed = demo_config.connectArgs.unsecureAllowed;


var prepareClient=function(){
	
  	//alert("client connected")
    roster = client.controllers.roster || new jabberwerx.RosterController(client);
    quickContacts = client.controllers.quickContact || new jabberwerx.cisco.QuickContactController(client);
    roster.autoaccept_in_domain = false;
    chat = client.controllers.chat || new jabberwerx.ChatController(client);
    muc = client.controllers.muc || new jabberwerx.MUCController(client);
    tabbedView = new jabberwerx.ui.TabbedView();
    tabbedView.render().appendTo("div.my_tabs");
    tabbedView.dimensions({width: 700, height: 420});
    tabbedView.addTab("introview", new sample.IntroView());
    var sendMediated = true;





    tabbedView.event("tabActivated").bind(function(evt) {
        var id = evt.data.id;
        $("input.remove_contact_btn").
            unbind("click").
            attr("disabled", "true").
            val("Remove Contact");
        $("input.room_invite").
            unbind("click").
            //attr("disabled", "true").
            val("Room Invite");
        $("input.remove_quick_contact_btn").
            unbind("click").
            attr("disabled", "true").
            val("Remove Quick Contact");
        $("input.room_config").
            unbind("click").
            attr("disabled", "true").
            val("Room Config");
        var session = evt.data.content.session;
        if (session) {
            if (session.getEntity() instanceof jabberwerx.Contact) {
                $("input.remove_contact_btn").
                    removeAttr("disabled").
                    val("Remove " + session.getEntity().getDisplayName()).
                    unbind("click").
                    click(function() {
                        session.getEntity().remove();
                        chat.closeSession(session.jid);
                        $("input.remove_contact_btn").
                            unbind("click").
                            attr("disabled", "true").
                            val("Remove Contact");
                        tabbedView.removeTab(id);
                    });

            }
        } else {
            room = evt.data.content._MucRoom;
            if (room) {
                $("document.span.room_invite").
                    /*removeAttr("disabled").
                    val("Invite To " + room.getDisplayName()).*/
                    unbind("click").
                    click(function() {
                    	
                        var ijid = prompt("Enter the invitee jid:", "");
                        var sentjids = room.invite([ijid], '', sendMediated);
                        var msg = 'Sent invite to ';
                        if (sentjids.length == 0) {
                            var msg = 'Could not send invite to "' + ijid + '".';
                        } else {
                            var msg = 'Sent invite to ' + sentjids[0] + ' using ' + (sendMediated ? 'mediated':'direct') + ' method.';
                        }
                        jabberwerx.util.debug.log(msg);
                        console.log(msg) 
                        sendMediated = !sendMediated; 
                    });

                $("input.room_config").
                    removeAttr("disabled").
                    val("Configure " + room.getDisplayName()).
                    unbind("click").
                    click(function() {
                        $("<div/>").
                            attr("id", room.getDisplayName()).
                            appendTo("div.muc_configs");
                        var configView = new jabberwerx.ui.MUCConfigView(room);
                        configView.event("viewRemoved").bind(function(evt) {
                            if (evt.data.get(0).jw_view.room.getDisplayName()) {
                                $("#" + evt.data.get(0).jw_view.room.getDisplayName()).remove();
                            }
                        });
                        configView.render().appendTo("div.muc_configs");
                        configView.dimensions({width:600, height:480});
                    });

            }
        }
    });
    if (this.client.clientStatus == jabberwerx.Client.status_connected) {
        $("div.my_auth").hide();
        // alert(" prepareClient in if")
         $("div.my_client").show();
         tabbedView.show();

         //set demo conference server if needed by groveling over entities to find it
         if (!demo_config.defaultConferenceServer) {
             //best shot at a default
             demo_config.demoConferenceServer = null;
             var ents = client.entitySet.toArray();
             for (var i = 0; i < ents.length; ++i) {
                 if (ents[i].hasIdentity("conference/text")) {
                     demo_config.demoConferenceServer = ents[i].jid.toString();
                     //jabberwerx.util.debug.log("Set demoConferenceServer to " + demo_config.demoConferenceServer);
                     break;
                 }
             }
             if (!demo_config.demoConferenceServer) {
                 demo_config.demoConferenceServer = 'conference@' + demo_config.connectArgs.domain;
                 //jabberwerx.util.debug.log("Set demoConferenceServer to best guess: " + demo_config.demoConferenceServer);
             }
         }

       
         //prsView.update();
         //rosterView.update();
         tabbedView.update();
     } else if (this.client.clientStatus == jabberwerx.Client.status_disconnected) {
         jQuery.each(tabbedView.getAllTabs(), function() {
             if (this.id != "introview") {
                 this.destroy();
             } else {
                 this.activate();
             }
         });
         tabbedView.hide();
         $("div.my_auth").show();
         // reset the in-band registration control & flag
         $("input:checkbox[name='in-band-reg']").prop("checked",false);
         auth._register = false;
         $("div.my_client").hide();

     } 
	
}
//application specific subclass
jabberwerx.app.BCCApp1 = jabberwerx.ui.JWApp.extend({

    /**
          *  Method invoked when the application is initially created. NOT invoked when application
          *  is loaded from store.
          *
          *  Applications should create JWA objects and bind to JWA events in this method
        */
    appCreate: function() {
        this._super();
        //create models controllers and views
       
        this.client = new jabberwerx.Client('BCCApp1');
       
        this.view = new jabberwerx.ui.AuthenticationView(this.client, demo_config.connectArgs.domain);
        //attach any JWA events
        this.client.event("clientStatusChanged").bind(this.invocation('_onClientStatusChanged'));
    },

    /**
          *  Method is invoked once the application is loaded (all JWA objects have been created/loaded and initialized.
          *   Method is also called after application creation (after appCreate has been called).
          *
          *  Application should render it's views and rebind any HTML events (these events are not persisted).
        */
    appInitialize: function() {
    	
    	 
        this._super();
        //render
        var popOverSettings = {
        	    placement: 'bottom',
        	    container: 'body',
        	    html: true,
        	    selector: '[class="closer"]', //Sepcify the selector here
        	    content: function () {
        	        return $('#popover-content').html();
        	    }
        	}

        	//$('body').confirmation(popOverSettings);
        
        client=this.client
        auth=this.view
        auth.allowAtSymbol = demo_config.atSymbolAllowed;
        auth.render().appendTo("div.my_auth");
        //$("div.my_auth").hide();
                this._setState(this.client.clientStatus);
        if(this.client.clientStatus==jabberwerx.Client.status_connected)
        	{
        	prepareClient();
        	}
       
        client.event('clientConnected').bind(function()
        		{
        			prepareClient();
        		})
        		
        
        //map html events here, dom is rebuilt at render time
        jabberwerx.$("button.logout").bind("click", this.invocation("_btnLogoutClick"));
        //finally check state and set view accordingly
        
        this._setState(this.client.clientStatus);
        
        
        
        
        

	    var locale = /locale=([^&]+)/.exec(window.location.search);
	    if (locale) {
	        locale = locale[1];
	    }
	    if (locale) {
	        try {
	            jabberwerx.l10n.load(locale);
	        } catch (ex) {
	            jabberwerx.util.debug.log("unable to load " + locale + ": " + ex);
	        }
	    }
	    $("table.debug tr.row-one").append("<td class='config'></td>");
	 /*   demoConfigurator(
	        $("td.config"),
	        demo_config.connectArgs,
	        function(newCfg) {
	            //it will be used next connection attempt
	            jabberwerx.$.extend(true, jabberwerx._config, newCfg);
	            //update auth view domain
	            auth.domain = jabberwerx._config.domain;
	        });
*/
	    /*client = new jabberwerx.Client('sampleclient' + Math.random());
	    client.event('clientConnected').bind(function()
	    		{
	    	alert('clientconnected');
	    	var roster = client.controllers.roster || new jabberwerx.RosterController(client);
	        var quickContacts = client.controllers.quickContact || new jabberwerx.cisco.QuickContactController(client);
	        roster.autoaccept_in_domain = false;
	        var chat = client.controllers.chat || new jabberwerx.ChatController(client);
	        var muc = client.controllers.muc || new jabberwerx.MUCController(client);
	    		})*/
	    /*var jwaConsole = new jabberwerx.ui.ConsoleView(client, {parentDOM: $("div.jwa_console")});
	    jwaConsole.render().appendTo($("div.jwa_console"));
	    jwaConsole.dimensions({height: $("div.jwa_console").height(), width: $("div.jwa_console").width()}) */

	    // log version (updating by build scripts)
	    //jabberwerx.util.debug.log("CAXL-release-2014.04.10787 SampleClient");
	    

	    var roster = client.controllers.roster || new jabberwerx.RosterController(client);
	    var quickContacts = client.controllers.quickContact || new jabberwerx.cisco.QuickContactController(client);
	    roster.autoaccept_in_domain = false;
	    var chat = client.controllers.chat || new jabberwerx.ChatController(client);
	    var muc = client.controllers.muc || new jabberwerx.MUCController(client);
	    //if defaultConferenceServer is not empty we can initialize the demoConferenceServer
	    if (demo_config.defaultConferenceServer) {
	        demo_config.demoConferenceServer = demo_config.defaultConferenceServer;
	        //jabberwerx.util.debug.log("Set demoConferenceServer to default: " + demo_config.demoConferenceServer);
	    } else {
	        demo_config.demoConferenceServer = 'conference@' + demo_config.connectArgs.domain;
	        //jabberwerx.util.debug.log("Set demoConferenceServer to best guess: " + demo_config.demoConferenceServer);
	    }
	    //var auth = new jabberwerx.ui.AuthenticationView(client, jabberwerx._config.domain);
	    //auth.allowAtSymbol = demo_config.atSymbolAllowed;

	    // bind on the AuthenticationView 'viewRendered' event to
	    // add the in-band register checkbox control
	    //auth.event("viewRendered").bind(function(evt){
/*	        var authForm = evt.source.jq;
	        var div = $("<div/>")
	            .addClass("auth_extra");
	        $("<input type='checkbox'/>")
	            .attr("name","in-band-reg")
	            .appendTo(div);
	        div.append("Create a new account");
	        authForm.append(div); */
	    //});

	    //auth.render().appendTo("div.my_auth");

	    // bind the clearing/setting of in-band register flag to
	    // the in-band register checkbox control
	    $("input:checkbox[name='in-band-reg']").bind("click", function() {
	       if ($(this).get(0).checked) {
	           auth._register = true;
	       } else {
	           auth._register = false;
	       }
	    });

	    var prsView = new jabberwerx.ui.SelfPresenceView(client, true);
	    prsView.allowUnavailable = true;
	    prsView.setStatusChoices(
	            "available",
	            prsView.getStatusChoices("available").concat("Custom..."));
	    prsView.setStatusChoices(
	            "away",
	            prsView.getStatusChoices("away").concat("Custom..."));
	    prsView.setStatusChoices(
	            "xa",
	            [].
	            concat(jabberwerx._("Extended Away")).
	            concat("PTO").
	            concat("Custom..."));
	    prsView.setStatusChoices(
	            "dnd",
	            prsView.getStatusChoices("dnd").concat("Custom..."));
	    prsView.event("presenceSelected").bind(function(evt) {
	        var view = evt.source;
	        var client = view.client;
	        var data = evt.data;

	        if (data.status == "Custom...") {
	            var status = prompt("Enter status message for " + evt.data.show, "");

	            if (!status) {
	                view.update();
	                return true;
	            }
	            var choices = view.getStatusChoices(evt.data.show);
	            var pos = jQuery.inArray("Custom...", choices);
	            if (pos > 0) {
	                choices.splice(pos, 0, status);
	            } else if (jQuery.inArray(status, choices) == -1) {
	                choices.push(status);
	            }

	            view.setStatusChoices(data.show, choices);
	            data.status = status;
	        }

	        return false;
	    });

	    prsView.render().appendTo(".my_presence");
	    prsView.update();

	    var rosterView = new jabberwerx.ui.RosterView(client.entitySet);
	    rosterView.event("rosterGroupingRendered").bind(function(evt) {
	       if (evt.data.grouping.name != "") {
	           evt.data.grouping.expand();
	       }
	    });
	    rosterView.setDefaultGroupingName("unknown");
	    rosterView.render().prependTo("div.my_roster");
	    rosterView.height(400);

	    $("input:checkbox[name='filter_contacts']").bind("click", function() {
	       if ($(this).get(0).checked) {
	           rosterView.addFilter(rosterView.invocation("filterByContacts"));
	       } else {
	           rosterView.removeFilter(rosterView.invocation("filterByContacts"));
	       }
	    });
	    $("input:checkbox[name='filter_active']").bind("click", function() {
	       if ($(this).get(0).checked) {
	           rosterView.addFilter(rosterView.invocation("filterByActive"));
	       } else {
	           rosterView.removeFilter(rosterView.invocation("filterByActive"));
	       }
	    }); 

	    /*var tabbedView = new jabberwerx.ui.TabbedView();
	    tabbedView.render().appendTo("div.my_tabs");
	    tabbedView.dimensions({width: 700, height: 420});
	    tabbedView.addTab("introview", new sample.IntroView());
	    var sendMediated = true;*/


	    roster.event("subscriptionReceived").bind(function(evt) {
	        if (evt.data.handled) {
	            return false;
	        }

	        var contact = evt.data.stanza.getFromJID();
	        var subView = new jabberwerx.ui.SubscriptionView(roster, contact, {group: "Demo Group"});
	        subView.render().appendTo("div.my_subscriptions");
	        subView.update();

	        subView.event("actionComplete").bind(function() {
	            subView.destroy();
	        });
	    });

	    chat.event("chatSessionOpened").bind(function(evt) {
	        var session = evt.data.chatSession;
	        var id = "chat11:" + session.jid;
	        var tab = tabbedView.getTab(id);
	        if (!tab) {
	            tab = tabbedView.addTab(id, new jabberwerx.ui.ChatView(session));
	        }
	    });

	    rosterView.event("rosterItemSelected").bind(function(evt) {
	        var item = evt.data.item;
	        var entity = item.entity;
	        if (entity instanceof jabberwerx.Contact) {
	            var id = "chat11:" + entity.jid.getBareJIDString();
	            var session = chat.openSession(entity.jid);
	            var tab = tabbedView.getTab(id);

	            if (tab) {
	                tab.activate();
	            }
	        } else if (entity instanceof jabberwerx.MUCRoom) {

	            var cbs = {
	                successCallback: function() {
	                    tab.show();
	                    tab.activate();
	                },
	                errorCallback: function(err, aborted) {
	                    tab.destroy();
	                    room.destroy();
	                    if (!aborted) {
	                        //alert("A problem occurred while trying to enter the room.\n" +
	                         //       " Details: " + jabberwerx.errorReporter.getMessage(err.getNode()));
	                    }
	                }
	            }

	            var id = "muc:" + entity.jid;
	            var room = muc.room(entity.jid);
	            var tab = tabbedView.getTab(id);
	            if (!tab) {
	                tab = tabbedView.addTab(id, new jabberwerx.ui.MucView(room));
	                tab.hide();
	            }
	            try {
	                room.enter(jabberwerx.JID.unescapeNode(client.connectedUser.jid.getNode()), cbs);
	            } catch (ex) {
	                //todo this is probably a roomactiveerror, an attempt to enter a room already in an enter attempt. do something
	                //jabberwerx.util.debug.log("Exception during room entry via roster: " + ex.message);
	            }
	        } else {
	            //alert("selected via " + evt.data.type + ": " + evt.data.item.entity);
	            return;
	        }

	    });

	   

	    muc.event("mucInviteReceived").bind(function(evt) {

	        // Create new MUCInviteView with the MUCInvite received
	        var mucInvite = evt.data;
	        var mucInviteView = new jabberwerx.ui.MUCInviteView(muc, mucInvite);
	        mucInviteView.render().appendTo("div.muc_invites");
	        mucInviteView.update();

	        // Set up tab for room now so that, upon joining, if messages from this room are received
	        // they aren't missed.
	        var room = muc.room(mucInvite.getRoom(),mucInvite.getReason());
	        var id = "muc:" + room.jid;
	        var tab = tabbedView.getTab(id);
	      
	        if (!tab) {
	        
	            tab = tabbedView.addTab(id, new jabberwerx.ui.MucView(room));
	            tab.hide();
	        }
	    
	        // Destroy the MUCInviteView when an action is completed
	        mucInviteView.event("actionComplete").bind(function(evtObj) {
	            mucInviteView.destroy();
	           
	            if (evtObj.data.action == jabberwerx.ui.MUCInviteView.ACTION_JOIN) {
	                var err = evtObj.data.error;
	               
	                if (err) {
	                    tab.destroy();
	                    room.destroy();
	                    //alert("A problem occurred while trying to enter the room.\n" +
	                      //    " Details: " +
	                        //  jabberwerx.errorReporter.getMessage(err.getNode()));
	                } else {
	                    tab.show();
	                    tab.activate();
	                }
	            } else {
	                tab.destroy();
	                room.destroy();
	            }
	        });
	    });

	    $("input.room_search").click(function() {
	        var roomSearch = new jabberwerx.ui.MUCSearchView(muc,
	            demo_config.demoConferenceServer);

	        roomSearch.event("actionComplete").bind(function(evt) {
	            try {
	                var room = evt.data.submitted;
	                var id = "muc:" + room.jid;
	                var tab = tabbedView.getTab(id);
	                if (!tab) {
	                    tab = tabbedView.addTab(id, new jabberwerx.ui.MucView(room));
	                    tab.hide();
	                }

	                //enter room callbacks
	                var cbs = {
	                    successCallback: function() {
	                        tab.show();
	                        tab.activate();
	                    },
	                    errorCallback: function(err, aborted) {
	                        tab.destroy();
	                        //cleanup partially entered room
	                        room.destroy();
	                        if (!aborted) {
	                            //alert("A problem occurred while trying to enter the room.\n" +
	                              //  " Details: " +
	                                //jabberwerx.errorReporter.getMessage(err.getNode()));
	                        }
	                    }
	                }

	                room.enter(jabberwerx.JID.unescapeNode(client.connectedUser.jid.getNode()),cbs);
	            } catch (e) {
	                //alert("An exception occurred while trying to create/join the MUC room.\n Details: " +
	                     //   e.message);
	            }

	            // Removes the MUC Search window.
	            roomSearch.destroy();
	        });

	        roomSearch.render().appendTo(".muc_search");
	        roomSearch.dimensions({width:600, height:480});
	    });

	    $("input.enter_room_btn").click(function() {
	        //var roomName = prompt("Enter the room name:", "");
	    	
	    	var roomName="sec_cr_alert"+Math.random()
	        if (roomName) {
	            try {
	                var tjid = jabberwerx.JID.asJID(roomName);
	                if (tjid.getNode() == '') {
	                    tjid = jabberwerx.JID.asJID(roomName + "@" + demo_config.demoConferenceServer);
	                }
	                room = muc.room(tjid.toString());
	                var id = "muc:" + room.jid;
	                var tab = tabbedView.getTab(id);
	                
	                if (!tab) {
	                
	                    tab = tabbedView.addTab(id, new jabberwerx.ui.MucView(room));
	                    tab.hide();
	                }
	                //enter room callbacks
	                var cbs = {
	                    successCallback: function() {
	                        tab.show();
	                        tab.activate();
	                    },
	                    errorCallback: function(err, aborted) {
	                        tab.destroy();
	                        //cleanup partially entered room
	                        room.destroy();
	                        if (!aborted) {
	                            //alert("A problem occurred while trying to enter the room.\n" +
	                              //      " Details: " + jabberwerx.errorReporter.getMessage(err.getNode()));
	                        }
	                    }
	                }
	                //define configure if not using default
	                /*if  (!$("input:checkbox[name='default_config']").get(0).checked) {
	                    cbs.configureCallback = function() {
	                         $("<div/>").
	                            attr("id", room.getDisplayName()).
	                            appendTo("div.muc_configs");
	                        var configView = new jabberwerx.ui.MUCConfigView(room);
	                        configView.event("viewRemoved").bind(function(evt) {
	                            if (evt.data.get(0).jw_view.room.getDisplayName()) {
	                                $("#" + evt.data.get(0).jw_view.room.getDisplayName()).remove();
	                            }
	                        });
	                        configView.render().appendTo("div.muc_configs");
	                        configView.dimensions({width:600, height:480});
	                    }
	                }*/

	                room.enter(jabberwerx.JID.unescapeNode(client.connectedUser.jid.getNode()),cbs);
	            } catch (e) {
	               // alert("An exception occurred while trying to create/join the MUC room.\n Details: " +
	                       // e.message);
	            }
	        }
	    });

	    $("input.add_contact_btn").click(function() {
	        var contact = prompt("Enter new contact jid: ", "");
	        if (contact) {
	            // If a QuickContact entity already exists then clear its groups
	            var entity = client.entitySet.entity(contact);
	            if (entity instanceof jabberwerx.cisco.QuickContact) {
	                entity.setGroups(null);
	            }
	            try {
	                roster.addContact(contact);
	            } catch(e) {
	               // alert("A problem occurred while trying to add the contact " + contact +
	                 //       ".\n Details: " + e.message);
	            }
	        }
	    });

	    $("input.add_quick_contact_btn").click(function() {
	        var contact = prompt("Enter quick contact jid: ", "");
	        if (contact) {
	            try {
	                quickContacts.subscribe(contact);
	                var entity = client.entitySet.entity(contact);
	                if (entity && entity instanceof(jabberwerx.cisco.QuickContact)) {
	                     entity.setGroups("Quick Contacts");
	                }
	            } catch(e) {
	               // alert("A problem occurred while trying to add quick contact " + contact +
	                 //       ".\n Details: " + e.message);
	            }
	        }
	    });
	 
	    //$("div.my_client").hide();
	    
	  
	   

    },

    /**
           * destroy any JWA objects this application explicitly created
         */
    destroy: function() {
        //
        this.client.event("clientStatusChanged").bind(this.invocation('_onClientStatusChanged'));

        this.client.destroy();
        this.client = null;
        this.view.destroy();
        this.view = null;
        this._super();
    },

    _onClientStatusChanged: function(evt) {
    		//alert("client status changed"+evt.data.next) 
        	 this._setState(this.client.clientStatus);
	        if (evt.data.next == jabberwerx.Client.status_connected) {
	           $("div.my_auth").hide();
	            //alert("in if")
	            $("div.my_client").show();
	            tabbedView.show();

	            //set demo conference server if needed by groveling over entities to find it
	            if (!demo_config.defaultConferenceServer) {
	                //best shot at a default
	                demo_config.demoConferenceServer = null;
	                var ents = client.entitySet.toArray();
	                for (var i = 0; i < ents.length; ++i) {
	                    if (ents[i].hasIdentity("conference/text")) {
	                        demo_config.demoConferenceServer = ents[i].jid.toString();
	                        //jabberwerx.util.debug.log("Set demoConferenceServer to " + demo_config.demoConferenceServer);
	                        break;
	                    }
	                }
	                if (!demo_config.demoConferenceServer) {
	                    demo_config.demoConferenceServer = 'conference@' + demo_config.connectArgs.domain;
	                    //jabberwerx.util.debug.log("Set demoConferenceServer to best guess: " + demo_config.demoConferenceServer);
	                }
	            }

	          
	            prsView.update();
	            rosterView.update();
	            tabbedView.update();
	        } else if (evt.data.next == jabberwerx.Client.status_disconnected) {
	            jQuery.each(tabbedView.getAllTabs(), function() {
	                if (this.id != "introview") {
	                    this.destroy();
	                } else {
	                    this.activate();
	                }
	            });
	            tabbedView.hide();
	            $("div.my_auth").show();
	            // reset the in-band registration control & flag
	            $("input:checkbox[name='in-band-reg']").prop("checked",false);
	            auth._register = false;
	            $("div.my_client").hide();

	        } 
       
    },

    _btnLogoutClick: function() {
    	
    //this.client.clientStatus=jabberwerx.Client.status_disconnected
     this.client.disconnect();
    	
    },

    _setState: function(state) {
    	//alert(state)
        switch (state) {
            case -1: //initializing
            	 jabberwerx.$("div.my_auth").hide();
                jabberwerx.$("button.logout").hide();
                jabberwerx.$("div.message").text('invalid until loaded');
                return;
            case jabberwerx.Client.status_connected:
            	jabberwerx.$("div.my_auth").hide();
                jabberwerx.$("button.logout").show();
                jabberwerx.$("div.my_client").show();
                break;
            case jabberwerx.Client.status_disconnected:
            	jabberwerx.$("div.my_auth").show();
                jabberwerx.$("button.logout").hide();
                jabberwerx.$("div.my_client").hide();
                break;
        }
        jabberwerx.$("div.message").text(this.client.getClientStatusString(state));
    }
}, "jabberwerx.app.BCCApp1");


/**
  *  Allow jabberwerx.app to control lifetime of application. Application is loaded from persisted store if possible
  *  otherwise a new instance is created. New instance is assigned to singleton jabberwerx.util.app._jwappinst.
*/

jabberwerx.util.persistedApplicationClass('jabberwerx.app.BCCApp1');


$ = jabberwerx.$;
jQuery = jabberwerx.$;

//copy sample client's connection arguments into CAXL's configuration
jabberwerx.$.extend(true, jabberwerx._config, demo_config.connectArgs);

sample = {};

sample.IntroView = jabberwerx.ui.JWView.extend({
    init: function() {
        this._super();
    },
    destroy: function() {
        this._super();
    },

    createDOM: function(doc) {
        var data = $("<div/>", doc).addClass("jabberwerx intro");

        $("<h2/>").appendTo(data).
                   text("Welcome to GSE chat");

        return data;
    },

    setTabControl: function(tab) {
        this._super(tab);

        if (tab) {
            tab.label("Welcome");
        }
    }
}, "sample.IntroView");

sample.IntroView.mixin(jabberwerx.ui.Tabbable);
   

		
var SampleClientInit=function()
{
	
	    // Check on a locale first...
	
	roster = client.controllers.roster || new jabberwerx.RosterController(client);
    quickContacts = client.controllers.quickContact || new jabberwerx.cisco.QuickContactController(client);
    roster.autoaccept_in_domain = false;
    chat = client.controllers.chat || new jabberwerx.ChatController(client);
    muc = client.controllers.muc || new jabberwerx.MUCController(client);
    tabbedView = new jabberwerx.ui.TabbedView();
    tabbedView.render().appendTo("div.my_tabs");	
    if (demo_config.defaultConferenceServer) {
        demo_config.demoConferenceServer = demo_config.defaultConferenceServer;
        //jabberwerx.util.debug.log("Set demoConferenceServer to default: " + demo_config.demoConferenceServer);
    } else {
        demo_config.demoConferenceServer = 'conference@' + demo_config.connectArgs.domain;
        //jabberwerx.util.debug.log("Set demoConferenceServer to best guess: " + demo_config.demoConferenceServer);
    }
	}
var showPortlet=function()
{
	$('#chat_portlet .portlet-title .expand').click();
	}
var callRoomCreate= function(data,room_id) {
	
	showPortlet();
  	 //var roomName = prompt("Enter the room name:", "");
	var roomName="sec_alert"+Math.random();
	
	
       if (roomName) {
    	   //alert(roomName)
           try {
               var tjid = jabberwerx.JID.asJID(roomName);
               if (tjid.getNode() == '') {
                   tjid = jabberwerx.JID.asJID(roomName + "@" + demo_config.demoConferenceServer);
               }
               room= muc.room(tjid.toString(),"sec_alert"+room_id);  
               var id = "muc:" + room.jid;
               var tab = tabbedView.getTab(id);
              
               if (!tab) {
               
                   tab = tabbedView.addTab(id, new jabberwerx.ui.MucView(room));
                   tab.hide();
               }
               //enter room callbacks
               var cbs = {
                   successCallback: function() {
                	   //alert("success")
                	   room.changeSubject("sec_alert"+room_id)
                       tab.show();
                       tab.activate();
                    	var sendMediated = true;
                        var ijid = data;
                        var sentjids = room.invite([ijid], "sec_alert"+room_id, sendMediated);
                        var msg = 'Sent invite to ';
                        if (sentjids.length == 0) {
                            var msg = 'Could not send invite to "' + ijid + '".';
                        } else {
                            var msg = 'Sent invite to ' + sentjids[0] + ' using ' + (sendMediated ? 'mediated':'direct') + ' method.';
                        }
                        jabberwerx.util.debug.log(msg);
                        sendMediated = !sendMediated;
                        $('#close_invite_modal').trigger('click');
                   },
                   errorCallback: function(err, aborted) {
                       tab.destroy();
                       //cleanup partially entered room
                       room.destroy();
                       if (!aborted) {
                           //alert("A problem occurred while trying to enter the room.\n" +
                             //      " Details: " + jabberwerx.errorReporter.getMessage(err.getNode()));
                       }
                   }
               }
               //define configure if not using default
               /*if  (!$("input:checkbox[name='default_config']").get(0).checked) {
                   cbs.configureCallback = function() {
                        $("<div/>").
                           attr("id", room.getDisplayName()).
                           appendTo("div.muc_configs");
                       var configView = new jabberwerx.ui.MUCConfigView(room);
                       configView.event("viewRemoved").bind(function(evt) {
                           if (evt.data.get(0).jw_view.room.getDisplayName()) {
                               $("#" + evt.data.get(0).jw_view.room.getDisplayName()).remove();
                           }
                       });
                       configView.render().appendTo("div.muc_configs");
                       configView.dimensions({width:600, height:480});
                   }
               }*/

               room.enter(jabberwerx.JID.unescapeNode(client.connectedUser.jid.getNode()),cbs);
           } catch (e) {
              // alert("An exception occurred while trying to create/join the MUC room.\n Details: " +
                     //  e.message);
           }
           
      
       }
       if($('#chat_modal').find('.modal-content').find('.modal-body').css('display') == 'none'){
			
			$('#chat_modal').find('.modal-content').find('.modal-body').show('slow');
			$('#chat_modal').find('button.full').show();
			$('#chat_modal').find('button.minus').show();
   	}
  }
 

var callRoomInvite= function(data) {
	//alert('in call room invite');
	var sendMediated = true; 
    var ijid = data;
    var sentjids = room.invite([ijid], '', sendMediated);
    var msg = 'Sent invite to ';
    if (sentjids.length == 0) {
        var msg = 'Could not send invite to "' + ijid + '".';
    } else {
        var msg = 'Sent invite to ' + sentjids[0] + ' using ' + (sendMediated ? 'mediated':'direct') + ' method.';
    }
    jabberwerx.util.debug.log(msg);
    sendMediated = !sendMediated;
    $('#close_invite_modal').trigger('click');
 
 }

var getjabberstatus=function(options) {
  	 //wrapped in an anon function to avoid polluting global namespace
  		//alert(client.isConnected())
  	 if(client.isConnected())
  	 {
  		 
  	  new jabberwerx.cisco.QuickContactController(client);

  	            new jabberwerx.RosterController(client);
  	                  var _roster = { 

  	                _presenceChange: function(contact){

  	                    
  	                     //alert('test0')
  	                    var presence = contact.getPrimaryPresence();
  	                     //alert(presence.getType())
  	                    var show = "unknown";
  	                    var status = "";
  	                    if (presence) {
  	                        show = presence.getType() ||
  	                        presence.getShow() ||
  	                        "available";
  	                        status = presence.getStatus();
  	                    }
  	                    show = " [" + show + "]" + (status ? "(" + status + ")" : "");
  	                    	//alert(show)
  	                    // sets entities status
  	                    var color="orange";
  	                    //alert(contact+show);
  	                    if(show == " [available]"  )
  	                    {
  	                    //alert("j")
  	                    color="green";
  	                    }
  	                    else if(show == " [xa]")
  	                    {
  	                    color="orange"
  	                    }
  	                    else if(show == " [dnd]" || show == " [xa](Presenting)" || show == " [ Presenting]" )
  	                    {
  	                    color="red"
  	                    }
  	                    else if(show ==" [unavailable]" )
  	                    {
  	                    color="black";
  	                    }
  	                    var id=contact.getDisplayName().substring(0, contact.getDisplayName().indexOf('@'));
  	                    $('#chatModal').clone().addClass('extra_class').appendTo("body").attr('id',id).find('*')
  	                    .each(function(){
  	                    var sub_id=$(this).attr('id')
  	                    $(this).attr('id',sub_id+id)
  	                    })
  	                    
  	                   
  	                
  	               
  	               
  	                    if(options.createRoom==true)
  	                    	{
  	                    	var room_id=options.room_id;
  	                    	//alert(room_id)
  	                    	//console.log( 'onClick=callRoomCreate("'+contact.getDisplayName()+'",'+room_id+')')
  	                    	contact._table_entry.children(".status").find('#font_b').html('<span data-value="'+contact.getDisplayName()+'" data-toggle="modal" data-target="#'+id+'" onClick=callRoomCreate("'+contact.getDisplayName()+'","'+room_id+'")    title="'+show+'" class="room_invite glyphicon glyphicon-user chat_open" style="color:'+color+';" ></span>');
  	                    	}
  	                    else
  	                    	{
  	                    	
  	                    	contact._table_entry.children(".status").find('#font_b').html('<span data-value="'+contact.getDisplayName()+'" data-toggle="modal" data-target="#'+id+'" onClick=callRoomInvite("'+contact.getDisplayName()+'")    title="'+show+'" class="room_invite glyphicon glyphicon-user chat_open" style="color:'+color+';" ></span>');
  	                    	}
  	                  
  	            
  	                },

  	                _updateItem: function(contact,name,role) {
  	                tstr=contact.getClassName()
  	                    tstr=(tstr.slice(tstr.lastIndexOf('.') + 1));
  	                    //alert(tstr);
  	                    if (!(contact instanceof jabberwerx.Contact ||tstr=="LocalUser")) {
  	                        return;
  	                    }

  	                   // if (!contact._table_entry) {
  	                    
  	                        contact._table_entry = $('<tr class="roster-item" style="border:0px;" ><td class="contact" style="width:60px;font-size:14px;padding-bottom: 1em;display:none;"></td><td class="name" style="width:200px;font-size:14px;border:0px;padding-bottom: 1em;font-weight: 500;text-shadow: 0px 0px #444444;"></td><td class="status " style="border:0px;"><div id="font_a" style="float:right;padding-bottom: 1em;padding-left: 1em;"><label id="font_b"></label></div></td></tr>');
  	                        contact._table_entry.appendTo(id);
  	                   // }
 
  	                    // set the entities display name
  	                    contact._table_entry.children(".contact").text(contact.getDisplayName() );
  	                   // contact._table_entry.children(".role").text(role.toUpperCase());
  	                    //name=name.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
  	                  contact._table_entry.children(".name").html('<a href="mailto:'+name+'@cisco.com" target="_top">'+name+'</a>');
  	                    
  	                    _roster._presenceChange(contact);
  	                },

  	               
  	                _removeItem: function(contact) {
  	                    if (contact._table_entry) {
  	                        contact._table_entry.remove();
  	                        delete contact._table_entry;
  	                    }
  	                }
  	            };
  	             var updateContact = function(contact,name,role){
  	                _roster._updateItem(contact,name,role);
  	               // $('#registerJID .registertext').attr('value','');
  	            }

  	           
  	            var onPresenceChanged = function(evt){
  	                
  	                // gets contact from current event object
  	                var contact = evt.source;

  	                _roster._presenceChange(contact);
  	            }

  	                 var id=options.id;
  	            // Map the global jQuery symbol to jabberwerx's jquery for convenience.
  	            //var $ = jabberwerx.$;

  	            var registerForPresence = function(jabber_client, jid,name,role, bind, update_contact){
  	          
  	                try{ 
  	                    //Using self registered controller
  	                  
  	                    jabber_client.controllers.quickContact.subscribe(jid);
  	                } catch(ex) {
  	                    
  	                    if(window.console){
  	                       // console.log('Subscribe: ' + jid + ' is a RosterContact.');
  	                    }
  	                } finally {
  	                   // alert(jid)
  	                    //alert(jabber_client.isConnected());
  	                    var contact = jabber_client.entitySet.entity(jid);
  	                    //console.log(contact)
  	                   // alert(jabber_client.entitySet.entity(jid))
  	                    //contact.setDisplayName(jid);
  	                    update_contact(contact,name,role);
  	                    contact.event('primarypresencechanged').bind(bind);
  	                }
  	            }
  	            	//alert('values')
  	               
  	               var values=options.values;
  	            	//console.log(options)
  	              /* var main=""
  	               var rows = $(id).find('> tbody > tr');
  	               alert("check");
  	               console.log(rows);
  	               console.log(options.values)
  	               alert("checked");
  	        rows.each(function(i,obj){
  	                 var single_row=$(this).find('td')
  	                 alert("in each row");
  	                 //alert($(this).find('td').html())
  	                  //$('#registerJID .registertext').val(single_row+"@cisco.com");
  	                // $('#registerJID .registerbutton').trigger('click');
  	                var col_content=""
  	                single_row.each(function(jj,rec){
  	                    var single_col=$(this).html();
  	                    alert(single_col)
  	                    
  	                    
  	                    col_content=col_content+single_col+'`~';
  	                    
  	                    
  	                   
  	                    if(jj==single_row.length-1) 
  	                    {
  	                    main =main+col_content +":"
  	                
  	                    }
  	                    
  	                })
  	                
  	                 
  	                 });*/
  	                 var temp=id.replace('#','').trim()
  	                //alert(temp);
  	               var table = document.getElementById(temp);
  	                 //alert("TABLE"+table.rows.length);
  	while(table.rows.length>1) {
     //  alert('test')
  	  table.deleteRow(table.rows.length-1);
  	  
  	};
  	//main=main.slice(0, -1);
  	//var arr=main.split(':')
  	//alert("main "+main);
  	               
  	        for(j=0;j<values.length;j++ ){
  	               // alert(arr[j]);
  	                      var arr_sub=[]
  	                      arr_sub[0]=values[j]
  	                    //console.log(values[0].userIds[j])
  	                        if(values[j].trim() != ''){
  	                        //alert(client);
  	                        try{
  	                        
  	                        
  	                 /*       if(flag==-1)
  	                    {
  	                    
  	                    flag_array.push(user_id)
  	                    chatSession_m.event("chatReceived").bind(function(evt) {
  	                        
  	                        if($('#'+user_id).hasClass('in')==false)
  	                        {
  	                        $('#chat_queue').css('background-color','#c00');
  	                        
  	                        
  	                        $('#chat_queue').html("<center><b><font size=4 color=#ffffff >Message Received from "+user_id+"</font></b></center>")
  	                        $('#chat_queue').attr('data-toggle','modal')
  	                        $('#chat_queue').attr('data-target','#'+user_id)
  	                        alert("chat received from "+user_id);
  	                            }
  	                            $('#chat_queue').attr('data-toggle','modal')
  	                        $('#chat_queue').attr('data-target','#'+user_id)
  	                            var message = evt.data;
  	                             if(message.getErrorInfo())
  	                             {
  	                            $(options).append('<p>'+"Error occured Message could not be delivered" +'</p><hr>' );
  	                            }
  	                            else
  	                            {
  	                            var x=message.getFrom().substring(0,message.getFrom().indexOf('@'))
  	                            var f_a=x.substring(0,1).toUpperCase()
  	                            $(options).append('<div class="row"><div class="col-md-1"><div class="numberCircle" style="background-color:#e3e1dd">'+f_a+'</div></div><div class="col-md-11"><p style="color:#888888;display:inline-block" ><b>'+ x + ": </b></p><p>" + message.getBody() +'</p></div></div>'+'<hr>')
  	                            }
  	                        });
  	                        chatSession_m.event("chatSent").bind(function(evt) {
  	                            alert("chat send")
  	                            var message = evt.data;
  	                            $(options).append('<div class="row"><div class="col-md-1"><div class="numberCircle" style="background-color:#a8c335">``</div></div><div class="col-md-11"><p style="color:#888888;display:inline-block" ><b>'+'You' + ": </b></p><p>" + message.getBody() +'</p></div></div>'+'<hr>')
  	                            
  	                            //console.log(message)
  	                     
  	                        });
  	                        flag=1

  	                    }
  	                    else{
  	                    flag=1
  	                    }*/
  	                        }
  	                        catch(e){
  	                    //alert("An exception occurred while trying to obtain a session : " + e.message);
  	                }
  	                        //alert(arr_sub[0])
  	                            registerForPresence(client, arr_sub[0]+'@cisco.com',arr_sub[0],"SME",onPresenceChanged, updateContact);
  	                        }
  	                 }
  	        //alert(id)
  	                    $(id).show();
  	              
  	          }
  	          else{
  	          var id=options.id;
  	          var values=options.values
  	          
  	        
  	          for(i=0;i<values.length;i++)
  	        	  {
  	        	 
  	        	$(id).append('<tr id=row'+i+'></tr>');
  	        	
    	          $('#row'+i).html('<td  style="font-size:14px;padding-bottom: 1em;border:0px;"><a href="mailto:'+values[i]+'@cisco.com" target="_top">'+values[i]+'</a></td>');
  	        	  }
  	        $(id).append('<tr id="abc"></tr>');
	          $('#abc').html('<td colspan=2 style="font-size:12px;padding-bottom: 1em;border:0px;"><button class="dummy_btn btn btn-primary" onClick="showPortlet()">Please Login to Jabber to view SME Availability</button></td>');
  	          
  	          $(id).show();
  	          }
  	        }

        