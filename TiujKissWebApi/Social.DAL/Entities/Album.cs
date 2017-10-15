using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace Social.DAL.Entities
{
    public class Album
    {
        /// <summary>
        /// 
        /// </summary>
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid AlbumID { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public virtual ICollection<AlbumDetail> AlbumDetails { get; set; }
    }
}