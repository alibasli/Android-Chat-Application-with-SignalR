using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.BL.ViewModels.Api
{
    public class LoginApiGetViewModel
    {
        public string Email { get; set; }
        public string Password { get; set; }
        public bool RememberMe { get; set; }
    }
    public class UserIdModel
    {
        public int userId { get; set; }
    }
}

