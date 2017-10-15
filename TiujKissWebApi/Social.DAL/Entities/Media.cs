using Social.DAL.Entities.Enums;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace Social.DAL.Entities
{
    /// <summary>
    /// 
    /// </summary>
    public class Media
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid MediaID { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public Guid AlbumID { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public Guid AlbumDetailID { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public DateTime DateCreated { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public bool IsProfilePicture { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string Thumbnail { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string Preview { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string Original { get; set; }
    }
}