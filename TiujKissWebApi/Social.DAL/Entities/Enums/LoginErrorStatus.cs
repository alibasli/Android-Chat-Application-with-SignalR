using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities.Enums
{   /// <summary>
    /// 
    /// </summary>
    public enum LoginErrorStatus
    {
        //[DisplayAttribute(Name = "STR_NOT_SELECTED", ResourceType = typeof(Resources.Resources))]
        Success,
        //[DisplayAttribute(Name = Resources.Resources.STR_FEMALE)]
        Error,
        //[DisplayAttribute(Name = Resources.Resources.STR_OTHER)]
        EmailNotCorrect,

        PasswordNotCorrect
    }
}