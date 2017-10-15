using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNet.Identity.EntityFramework;
using System.Security.Claims;
using Microsoft.AspNet.Identity;
using System.ComponentModel;
using Social.DAL.Entities.Enums;

namespace Social.DAL.Entities
{
    /// <summary>
    /// Social User class
    /// </summary>
    public class User : IdentityUser <Guid, UserLogin, UserRole, UserClaim>
    {
        public async Task<ClaimsIdentity> GenerateUserIdentityAsync(UserManager manager, string authenticationType)
        {
            // Note the authenticationType must match the one defined in CookieAuthenticationOptions.AuthenticationType
            var userIdentity = await manager.CreateIdentityAsync(this, authenticationType);
            // Add custom user claims here
            return userIdentity;
        }

        /// <summary>
        /// User Album
        /// </summary>        
        public Guid AlbumID { get; set; }


        /// <summary>
        /// Password
        /// </summary>
        [DataType(DataType.Password)]
        public string Password { get; set; }

        /// <summary>
        /// Name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Lastname
        /// </summary>
        public string Lastname { get; set; }

        /// <summary>
        /// Birthdate
        /// </summary>
        public DateTime BirthDate { get; set; }

        /// <summary>
        /// Date Created
        /// </summary>
        [Required]
        public DateTime DateCreated { get; set; }

        /// <summary>
        /// Last Login
        /// </summary>
        [Required]
        public DateTime LastLogin { get; set; }

        /// <summary>
        /// is Active
        /// </summary>
        [Required]
        [DefaultValue(true)]
        public bool IsActive { get; set; }

        /// <summary>
        /// Is Item Deleted
        /// </summary>
        [Required]
        [DefaultValue(false)]
        public bool IsDeleted { get; set; }

        /// <summary>
        /// Date Modified
        /// </summary>
        [Required]
        public DateTime Modified { get; set; }

        /// <summary>
        /// Profile Photo
        /// </summary>
        public string ProfilePhoto { get; set; }

        public Gender Gender { get; set; }

        /// <summary>
        /// Profile Photo
        /// </summary>
        public string CoverPhoto { get; set; }

        /// <summary>
        /// Address
        /// </summary>
        public string Address { get; set; }

        /// <summary>
        /// Nationality
        /// </summary>
        public string Nationality { get; set; }


        public string Country { get; set; }
        public string City { get; set; }


        public virtual ICollection<Post> Posts { get; set; }



    }
}
