using Social.DAL.Entities.Enums;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.BL.ViewModels.Api
{
   public class LoginApiViewModel
    {
        public Guid UserID { get; set; }

        public string Name { get; set; }

        public string LastName { get; set; }

        public string Email { get; set; }

        public DateTime BirthDate { get; set; }

        public Gender Gender { get; set; }

        public LoginErrorStatus LoginErrorStatus { get; set; }
    }
}
