#!/usr/bin/perl

my @concurrenttimes;
my $abortSum = 0;

for (my $i = 0; $i < 10; $i++) {
   my $output = `java ConBoruvkaMain`;
   if ($? != 0) {
      print "$output";
      $i = 1000;
   }
   while ($output =~ /Abortcount is ([0-9]*)/g) {
      $abortSum += $1;
   }
   if ($output =~ /[0-9]*\.[0-9]*\nTotal execution time: ([0-9]*)/){
      push @concurrenttimes, $1;
   } else {
      print "$output\n";
      print "Output is messed up.\n";
      break;
   }
}
my $sum = 0;
foreach $value (@concurrenttimes){
   $sum += $value;
}
my $mean = $sum / (scalar @concurrenttimes);
my $abortmean = $abortSum / (scalar @concurrenttimes);
print "The mean time was $mean\nThe mean abort count was $abortmean\n";
