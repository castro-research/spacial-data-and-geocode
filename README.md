# Spacial Data and Geocode

## Problem

Many consultant (i.e Network service, Real Estate, etc...) provide service within a specific geographic area, such as a neighborhood, city, province or region.

Users may want to discover consultants near their current location or search for services within a specific city, district, neighborhood or other defined area.

How should we design a solution that supports both location-based discovery around the user and searches targeting a specific geographic area?

# Goal

We can solve this with many kind of database, as: MongoDB, ElasticSearch, SQL Server, and many complete database system that supports GeoSpacial system.

The purpose here is not showing a benchmark, but showing how can we solve with PostGIS implementation.

## Breaking down the problem

### Define what is region or area

We can represent a region or area with different data structure.

[**WORLD GEODETIC SYSTEM 1984**](https://epsg.org/home.html) is the most widely used geographic coordinate system for the earth.

![earth](https://images.openai.com/static-rsc-4/EHXYi5_Ra43U30_xCXZk4SzuCT8q0Cqi4iCwBAoZHOSoXt4zc2VZFOag_v-r862EWbe3_sW3BtVo0StKRSFBAY3dsTrZWVykOryGM9EhRlY6eiCP4YNLeQ-v_6zMCUgvK_sGjDG_EsFpNVkExYIbTle1hsWamREUEIqxeDZkDOtE_uShhD6NVrWHgNWCMOMG?purpose=fullsize)

"WGS84 comprises a standard coordinate frame for the Earth, a datum/reference ellipsoid for raw altitude data."

Reference: https://gis.stackexchange.com/questions/3334/difference-between-wgs84-and-epsg4326

Note that EPSG:4326 is official identifier for WGS84.

**Why is this important ?**

In PostGIS we use [Spatial Reference Identifier - SRID 4326](https://postgis.net/workshops/postgis-intro/geometries.html)

This identifier tell to the database that we are using the EPSG:4326 geometric system.

[!NOTE]
Many SRIDs exist for different coordinate reference systems, such as WGS84, Web Mercator, and UTM. Each SRID defines how spatial coordinates are represented and interpreted.

### User location

### Discovering / Searching Services