using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
    /// <summary>
    /// 
    /// </summary>
    public class UserRole : IdentityUserRole<Guid>
    {
        ///// <summary>
        ///// ID
        ///// </summary>
        //[Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        //public Guid UserRoleID { get; set; }

        ///// <summary>
        ///// User ID
        ///// </summary>
        //[Required]
        //public Guid UserID { get; set; }

        ///// <summary>
        ///// Role ID
        ///// </summary>
        //[Required]
        //public Guid RoleID { get; set; }


        ///// <summary>
        ///// Users
        ///// </summary>
        //public virtual User Users { get; set; }

        ///// <summary>
        ///// Roles
        ///// </summary>
        //public virtual Role Roles { get; set; }
    }
}
