#!/usr/bin/perl

use warnings;
use strict;
use Getopt::Std;

my %options=();
getopts("d:sml", \%options);
my $OUTPUT_DIRECTORY = '.';
if (defined $options{d}) {
   $OUTPUT_DIRECTORY = $options{d};
}
my $n = 0;
if (defined $options{s}) {
   $n = 10;
} elsif (defined $options{m}) {
   $n = 100;
} elsif (defined $options{l}) {
   $n = 10000;
} else {
   print "Error: input with -s, -m or -l for small medium or large graph respectively.\n";
   exit(1);
}

my $e = int($n / 3);
open(my $fh, ">", "$OUTPUT_DIRECTORY/graph.txt");
print $fh "$n $e\n";
my $start = int(rand($n));
my $prev = $start;
$prev = int(rand($n)) while ($prev == $start);
my $w = rand($n);
print $fh "$start $prev $w\n";
for (my $i = 0; $i < $e; $i=$i+1) {
   my $u = $prev;
   $u = int(rand($n)) while ($prev == $u);
   my $weight = rand(10);
   print $fh "$u $prev $weight\n";
   $prev = $u;
}

close $fh;
   
