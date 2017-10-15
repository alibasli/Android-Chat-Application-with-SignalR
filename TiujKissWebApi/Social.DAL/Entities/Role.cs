using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
    /// </summary>
    /// Social User Role
    /// </summary>
    public class Role : IdentityRole<Guid, UserRole>
    {
        /////// <summary>
        /////// ID
        /////// </summary>
        ////[Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        ////public Guid RoleID { get; set; }

        /////// <summary>
        /////// Role Name
        /////// </summary>
        ////[Required]
        ////public string Name { get; set; }

        ///// <summary>
        ///// Role Description
        ///// </summary>
        //public string Description { get; set; }

        ///// <summary>
        ///// User Roles
        ///// </summary>
        //public List<UserRole> UserRoles { get; set; }
    }
}
