#!/vendor/bin/sh
#
# Copyright (C) 2022 Paranoid Android
# Copyright (C) 2022 CarbonROM
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set_read_ahead() {
    echo "$1" > /sys/class/block/mmcblk0/queue/read_ahead_kb
}

get_emmc_size() {
    blocks=$(cat /sys/class/block/mmcblk0/size)
    bytes=$(expr $blocks \* 512)
    echo "$(expr $bytes / 1024 / 1024 / 1024)"
}

if [ "$(get_emmc_size)" -gt 32 ]; then
    set_read_ahead 3072
elif [ "$(get_emmc_size)" -gt 16 ]; then
    set_read_ahead 2048
else
    set_read_ahead 1024
fi
