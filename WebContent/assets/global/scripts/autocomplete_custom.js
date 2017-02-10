$.widget( "custom.catcomplete", $.ui.autocomplete, {
        _create: function() {
          this._super();
          this.widget().menu( "option", "items", "> :not(.ui-autocomplete-category)" );
        },
        _renderMenu: function( ul, items ) {
          var that = this,
            currentCategory = "";
          $.each( items, function( index, item ) {
            var li;
            console.log(item)
            if ( item.type != currentCategory ) {
              ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
              currentCategory = item.type;
            }
            li = that._renderItemData( ul, item );
            if ( item.type ) {
              li.attr( "aria-label", item.type + " : " + item.search_string );
            }
          });
        }
      });