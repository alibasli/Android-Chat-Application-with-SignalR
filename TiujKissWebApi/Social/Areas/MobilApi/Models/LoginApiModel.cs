using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Social.Areas.MobilApi
{
    public class LoginApiModel
    {
        public string Username { get; set; }
        public string Password { get; set; }
    }
    public class UserIdModel
    {
        public int userId { get; set; }
    }
}