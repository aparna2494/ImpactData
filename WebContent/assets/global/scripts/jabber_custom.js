(function($){
var client = new jabberwerx.Client();
var chatController = new jabberwerx.ChatController(client);
var flag_array=[];
 $.fn.extend({

connectToJabber:function(username,password)

{

var demo_config = {
               httpBindingURL: "https://im3.ciscowebex.com/http-bind",
                    domain: "cisco.com",
                    username: username,
                    password: password,
                    bindingproxy : "https://im3.ciscowebex.com/http-bind",
	domain : "cisco.com",
	legacyAuth : true,
	maxGraphAge : 3000,
	appTag : "FirstContact-appv1"
            };
               var onConnected =function(){
               
              //alert('success');
              $('#close_modal').click();
             // $('#sme_jabber_table').getjabberstatus({id:'#sme_jabber_table',values:[]});      
                    
                };
               var onClientError =function(){
               
              
                    $('#error_jabber').show();
                    
                }; 
                ind=username.indexOf('@')
                if(ind!=-1)
                {
                username=username.substring(0,ind)
                }
             var username = username + "@" + demo_config.domain;
                    // The password that will be used for the client authentication
                    var password = password;
                    var connectArgs = {
                        // The proxy url to the BOSH server
                        httpBindingURL: demo_config.httpBindingURL,

                        successCallback: onConnected,
                        errorCallback: onClientError
                        
                    };
                    //alert("in connect");
                    client.connect(username, password, connectArgs);


},

 getjabberstatus:function(options) {
 //wrapped in an anon function to avoid polluting global namespace
	 
 if(client.isConnected())
 {
	// alert('in if');
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
                    
                   console.log('a')
                
               
               
                
                    contact._table_entry.children(".status").find('#font_b').html('<span data-value="'+contact.getDisplayName()+'" data-toggle="modal" data-target="#'+id+'"   title="'+show+'" class="glyphicon glyphicon-user chat_open" style="color:'+color+';" ></span>');
            
                },

                _updateItem: function(contact,name,role) {
                tstr=contact.getClassName()
                    tstr=(tstr.slice(tstr.lastIndexOf('.') + 1));
                    //alert(tstr);
                    if (!(contact instanceof jabberwerx.Contact ||tstr=="LocalUser")) {
                        return;
                    }

                   // if (!contact._table_entry) {
                    
                        contact._table_entry = $('<tr class="roster-item" ><td class="contact" style="width:60px;font-size:14px;padding-bottom: 1em;display:none;"></td><td class="name" style="width:200px;font-size:14px;padding-bottom: 1em;font-weight: 500;text-shadow: 0px 0px #444444;"></td><td class="role" style="width:40px;font-size:14px;padding-bottom: 1em;padding-left: 1em;"></td><td class="status"><div id="font_a" style="float:right;padding-bottom: 1em;padding-left: 1em;"><label id="font_b"></label></div></td></tr>');
                        contact._table_entry.appendTo(id);
                   // }

                    // set the entities display name
                    contact._table_entry.children(".contact").text(contact.getDisplayName() );
                    contact._table_entry.children(".role").text(role.toUpperCase());
                    name=name.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
                    console.log(name)
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
            //alert(jid);
            //alert(jabber_client)
                try{
                    //Using self registered controller
                    //alert(jid)
                    jabber_client.controllers.quickContact.subscribe(jid);
                } catch(ex) {
                  //  alert("in catch")
                    if(window.console){
                        console.log('Subscribe: ' + jid + ' is a RosterContact.');
                    }
                } finally {
                   // alert(jid)
                    //alert(jabber_client.isConnected());
                    var contact = jabber_client.entitySet.entity(jid);
                   // alert(jabber_client.entitySet.entity(jid))
                    contact.setDisplayName(jid);
                    update_contact(contact,name,role);
                    contact.event('primarypresencechanged').bind(bind);
                }
            }
           
               
               var values=options.values;
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
                
               var table = document.getElementById(temp);
                 
while(table.rows.length>1) {

  table.deleteRow(table.rows.length-1);
  
};
//main=main.slice(0, -1);
//var arr=main.split(':')
//alert("main "+main);
              // alert(arr.length); 
             
        for(j=0;j<values.length;j++ ){
               // alert(arr[j]);
                      var arr_sub=[]
                      arr_sub[0]=values[j].user_name
                      arr_sub[1]=values[j].full_name;
                      arr_sub[2]=values[j].role;
                        if(values[j].user_name.trim() != ''){
                        //alert(client);
                        try{
                        var chatSession_m = chatController.openSession(arr_sub[0]+ "@" + "cisco.com");
                        user_id=arr_sub[0]
                        flag=$.inArray(user_id, flag_array)
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
                        console.log(arr_sub[0]+'@cisco.com')
                            registerForPresence(client, arr_sub[0]+'@cisco.com',arr_sub[1],arr_sub[2],onPresenceChanged, updateContact);
                        }
                 }
                    $(id).show();
                    
                    
                

            
          
           

           
            

            
           
           

          }
          else{
          var id=options.id;
          
          $(id).append('<tr id="abc"></tr>');
          $('#abc').html('<td colspan=2 style="font-size:14px;padding-bottom: 1em;"><button class="dummy_btn" data-toggle="modal" data-target="#myModal" data-backdrop="static" data-keyboard="false" >Please Connect to Jabber to view SME</button></td>');
          $(id).show();
          }
        },
/* chat_controller:function(options)
{
//alert('in chat_controller')
     chatController.event("chatSessionOpened").bind(function(evt) { 
                     
                    var chatSession = evt.data.chatSession;
                    //alert(chatSession)
                         chatSession.event("chatReceived").bind(function(evt) {
                        //alert("azad");
                       
                            var message = evt.data;
                             if(message.getErrorInfo())
                             {
                            $(options).append("Error occured Message could not be delivered" +'\r\n' );
                            }
                            else
                            {
                            $(options).append(message.getFrom() + ": " + message.getBody() +'\r\n' );
                            }
                        });
                        chatSession.event("chatSent").bind(function(evt) {
                           // alert("parma")
                            var message = evt.data;
                            //console.log(message)
                            $(options).append('me' + ": " + message.getBody() +'\r\n' );
                        });
                    });
},*/
   send_message:function(options,user_id)
{
    try{
   
                    /**
                     * openSession is passed the bare jid of the user we want to
                     * chat with and returns an existing session or creates a
                     * new one for us. The method may throw an exception on
                     * bad arguments or invalid state and should be wrapped in
                     * a try-catch block.
                     *
                     * see ../api/symbols/jabberwerx.ChatController.html#openSession
                     */
                   
                    // alert('in send message'+flag_array)
                    var chatSession_m = chatController.openSession(user_id+ "@" + "cisco.com");
                   
                   
                    /**
                     * sendMessage is passed a plaintext message to send. The
                     * method may throw an exception on bad arguments or invalid
                     * state and should be wrapped in a try-catch block.
                     *
                     * see ../api/symbols/jabberwerx.ChatSession.html#sendMessage
                     */
                     flag=$.inArray(user_id, flag_array)
                    if(flag==-1)
                    {
                    flag_array.push(user_id)
                    chatSession_m.event("chatReceived").bind(function(evt) {
                       // alert("chat received");
                        if($('#'+user_id).hasClass('in')==false)
                        {
                        $('#chat_queue').css('background-color','#c00');
                        
                        
                        $('#chat_queue').html("<center><b><font size=4 color=#ffffff >Message Received from "+user_id+"</font></b></center>")
                        $('#chat_queue').attr('data-toggle','modal')
                        $('#chat_queue').attr('data-target','#'+user_id)
                        //alert("chat received from "+user_id);
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
                           // alert("chat send")
                            var message = evt.data;
                            $(options).append('<div class="row"><div class="col-md-1"><div class="numberCircle" style="background-color:#a8c335">``</div></div><div class="col-md-11"><p style="color:#888888;display:inline-block" ><b>'+'You' + ": </b></p><p>" + message.getBody() +'</p></div></div>'+'<hr>')
                            
                            //console.log(message)
                     
                        });
                        flag=1

                    }
                    else{
                    flag=1
                    }
                    chatSession_m.sendMessage($(this).val().trim());
                    $(this).val("")
                }
                catch(e){
                    //alert("An exception occurred while trying to obtain a session and send a message: " + e.message);
                }
}


        });
        })(jQuery);
