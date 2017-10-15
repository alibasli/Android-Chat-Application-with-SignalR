using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Social.DAL.Entities.Enums
{
    /// <summary>
    /// 
    /// </summary>
    public enum Gender
    {
        //[DisplayAttribute(Name = "STR_NOT_SELECTED", ResourceType = typeof(Resources.Resources))]
        Male,
        //[DisplayAttribute(Name = Resources.Resources.STR_FEMALE)]
        Female,
        //[DisplayAttribute(Name = Resources.Resources.STR_OTHER)]
        Other
    }
}
