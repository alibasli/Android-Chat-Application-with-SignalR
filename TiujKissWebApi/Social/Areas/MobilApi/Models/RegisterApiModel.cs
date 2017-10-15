using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Social.Areas.MobilApi
{
    public class RegisterApiModel
    {
        public string Email { get; set; }
        public string Password { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public string Birthday { get; set; }
        public string Gender { get; set; }
    }
}