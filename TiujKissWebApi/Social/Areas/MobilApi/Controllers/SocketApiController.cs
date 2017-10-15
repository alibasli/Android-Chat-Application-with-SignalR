using Microsoft.Web.WebSockets;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace Social.Areas.MobilApi
{
    public class SocketApiController : ApiController
    {
        [HttpGet,Route("Api/Socket/{username}")]
        public HttpResponseMessage Get(string username)
        {
            HttpContext.Current.AcceptWebSocketRequest(new ChatWebSocketHandler(username));
            return Request.CreateResponse(HttpStatusCode.SwitchingProtocols);
        }
     
        class ChatWebSocketHandler : WebSocketHandler
      {
           private static WebSocketCollection _chatClients = new WebSocketCollection();
           private string _username;
    
           public ChatWebSocketHandler(string username)
           {
               _username = username;
           }
   
           public override void OnOpen()
           {
               _chatClients.Add(this);
           }
    
           public override void OnMessage(string message)
           {
               _chatClients.Broadcast(_username + ": " + message);
           }
       }
    }
}
