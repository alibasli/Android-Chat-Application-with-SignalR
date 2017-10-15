using Social.DAL.Entities.Enums;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace Social.DAL.Entities
{
    /// <summary>
    /// 
    /// </summary>
    public class AlbumDetail
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid AlbumDetailID { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public Guid AlbumID { get; set; }

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
        /// Modification Status
        /// </summary>
        [Required]
        public ModificationStatus ModificationStatus { get; set; }


        /// <summary>
        /// 
        /// </summary>
        public string  Name { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public int NumberOfItems { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public DateTime DateCreated  { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public DateTime LastUpdate { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public AlbumType AlbumType { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public virtual ICollection<Media> Medias { get; set; }
    }
}