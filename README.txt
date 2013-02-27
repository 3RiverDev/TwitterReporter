TwitterReporter: Breaking News Detection and Visualization through the Geo-Tagged Twitter Network

Twitter provides a constant stream of concise data, useful within both geospatial and temporal domains.  This project attempts to accomplish a useful and interesting task: using live Twitter data to automatically identify breaking news events in near real-time.

Please see TwitterReporter.pdf for more details.

TwitterReporter was granted increased access to the location-based streaming filter Twitter API.  The removed rate-limiting effectively gives us all live, geotagged tweets in the continental US.  Although this is important for data quality, the library can still be used on any account.

The bot processes and cleanses incoming data.  Resulting usable tweets are run through a simple document-frequency (DF) algorithm.  Other algorithms were experiemented with (IDF, etc.), but thrown out for various reasons (see the paper for explanations).  If a topic is found, it is stored along with the tweet that composed it.

The hope is that stored topics can be displayed on a geographic visualization.  Google Maps apps, etc. are envisioned.

The Java/XML system is built on the following:

- Twitter4J
- Hibernate ORM
- Apache Lucene

CONTRIBUTIONS ARE WELCOME!  Feel free to contact me with any questions!

Licensed under the GNU Lesser General Public License (LGPL) v3.0