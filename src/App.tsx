import React, { useState } from 'react';
import {
  Phone,
  Shield,
  Search,
  Star,
  CheckCircle,
  Clock,
  MapPin,
  Calendar,
  Users,
  DollarSign,
  UserCheck,
  Plus,
  ArrowLeft,
  ChevronRight,
  Bell,
  Settings,
  HelpCircle,
  LogOut,
  Award,
  Filter,
  Check,
  X,
  FileText
} from 'lucide-react';

type Role = 'CUSTOMER' | 'WORKER' | 'ADMIN';
type Screen =
  | 'SPLASH'
  | 'LOGIN'
  | 'OTP'
  | 'REGISTRATION'
  | 'CUSTOMER_HOME'
  | 'WORKER_SEARCH'
  | 'WORKER_PROFILE'
  | 'BOOKING'
  | 'BOOKING_SUMMARY'
  | 'BOOKING_SUCCESS'
  | 'BOOKING_HISTORY'
  | 'WORKER_DASHBOARD'
  | 'WORKER_REQUEST'
  | 'WORKER_ONBOARDING'
  | 'EARNINGS'
  | 'REVIEW'
  | 'ADMIN_DASHBOARD'
  | 'ADMIN_VERIFICATION'
  | 'ADMIN_CATEGORY';

interface Worker {
  id: string;
  name: string;
  category: string;
  rating: number;
  reviewsCount: number;
  hourlyRate: number;
  experienceYears: number;
  city: string;
  isVerified: boolean;
  isAvailable: boolean;
  bio: string;
  skills: string[];
  photoUrl: string;
  aadhaarNumber?: string;
  isAadhaarVerified?: boolean;
}

interface Booking {
  id: string;
  customerName: string;
  workerName: string;
  category: string;
  eventDate: string;
  startTime: string;
  endTime: string;
  staffQuantity: number;
  location: string;
  fullAddress: string;
  subtotal: number;
  serviceFee: number;
  total: number;
  status: 'PENDING' | 'ACCEPTED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
}

const INITIAL_WORKERS: Worker[] = [
  {
    id: 'w1',
    name: 'Rajesh Kumar',
    category: 'Waiter',
    rating: 4.9,
    reviewsCount: 38,
    hourlyRate: 300,
    experienceYears: 4,
    city: 'Mumbai',
    isVerified: true,
    isAvailable: true,
    bio: 'Professional banquet & fine dining waiter specialized in luxury wedding catering.',
    skills: ['Banquet Service', 'Fine Dining', 'Silverware Handling'],
    photoUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200',
    aadhaarNumber: 'XXXX-XXXX-4921',
    isAadhaarVerified: true
  },
  {
    id: 'w5',
    name: 'Priya Sharma',
    category: 'Waiter',
    rating: 4.9,
    reviewsCount: 42,
    hourlyRate: 350,
    experienceYears: 5,
    city: 'Mumbai',
    isVerified: true,
    isAvailable: true,
    bio: 'Experienced head waitress & event captain for VIP galas, weddings, and fine dining banquets.',
    skills: ['Banquet Captain', 'VIP Table Service', 'Team Coordination'],
    photoUrl: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200',
    aadhaarNumber: 'XXXX-XXXX-8812',
    isAadhaarVerified: true
  },
  {
    id: 'w6',
    name: 'Karan Malhotra',
    category: 'Waiter',
    rating: 4.8,
    reviewsCount: 27,
    hourlyRate: 320,
    experienceYears: 4,
    city: 'Mumbai',
    isVerified: true,
    isAvailable: true,
    bio: 'Courteous and punctual waiter specialized in luxury wedding receptions and corporate buffets.',
    skills: ['Wedding Service', 'Champagne Pass', 'Tray Handling'],
    photoUrl: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200',
    aadhaarNumber: 'XXXX-XXXX-3349',
    isAadhaarVerified: true
  },
  {
    id: 'w7',
    name: 'Suresh Patil',
    category: 'Waiter',
    rating: 4.7,
    reviewsCount: 18,
    hourlyRate: 280,
    experienceYears: 3,
    city: 'Mumbai',
    isVerified: true,
    isAvailable: true,
    bio: 'High-energy waiter experienced in large-scale event setups, table service, and guest management.',
    skills: ['Buffet Setup', 'Table Service', 'Order Management'],
    photoUrl: 'https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=200',
    aadhaarNumber: 'XXXX-XXXX-1102',
    isAadhaarVerified: true
  },
  {
    id: 'w2',
    name: 'Amit Patel',
    category: 'Bartender',
    rating: 4.8,
    reviewsCount: 24,
    hourlyRate: 600,
    experienceYears: 5,
    city: 'Mumbai',
    isVerified: true,
    isAvailable: true,
    bio: 'Craft cocktail mixologist with experience at top lounge bars and high-profile private events.',
    skills: ['Mixology', 'Cocktail Menu', 'Bar Management'],
    photoUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200',
    aadhaarNumber: 'XXXX-XXXX-7410',
    isAadhaarVerified: true
  },
  {
    id: 'w3',
    name: 'Chef Vikram Singh',
    category: 'Chef',
    rating: 5.0,
    reviewsCount: 19,
    hourlyRate: 1200,
    experienceYears: 8,
    city: 'Mumbai',
    isVerified: true,
    isAvailable: true,
    bio: 'Live counter specialist in North Indian & Continental cuisines for corporate events.',
    skills: ['Live Counter', 'North Indian', 'Menu Planning'],
    photoUrl: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200',
    aadhaarNumber: 'XXXX-XXXX-9023',
    isAadhaarVerified: true
  }
];

export default function App() {
  const [currentScreen, setCurrentScreen] = useState<Screen>('SPLASH');
  const [userRole, setUserRole] = useState<Role>('CUSTOMER');
  const [phone, setPhone] = useState('9876543210');
  const [otp, setOtp] = useState('123456');
  const [userName, setUserName] = useState('Rahul Sharma');
  const [userCity, setUserCity] = useState('Mumbai');

  const [workers, setWorkers] = useState<Worker[]>(INITIAL_WORKERS);
  const [selectedWorker, setSelectedWorker] = useState<Worker | null>(INITIAL_WORKERS[0]);
  const [searchCategory, setSearchCategory] = useState<string>('All');

  // Booking Form State
  const [bookingDate, setBookingDate] = useState('15 Aug 2026');
  const [startTime, setStartTime] = useState('06:00 PM');
  const [endTime, setEndTime] = useState('11:00 PM');
  const [staffQty, setStaffQty] = useState(2);
  const [venue, setVenue] = useState('St. Regis Hotel, Lower Parel');
  const [address, setAddress] = useState('Senapati Bapat Marg, Mumbai');
  const [lastBookingId, setLastBookingId] = useState('BK-84920');

  const [bookings, setBookings] = useState<Booking[]>([
    {
      id: 'BK-84920',
      customerName: 'Rahul Sharma',
      workerName: 'Rajesh Kumar',
      category: 'Waiter',
      eventDate: '15 Aug 2026',
      startTime: '06:00 PM',
      endTime: '11:00 PM',
      staffQuantity: 2,
      location: 'St. Regis Hotel, Lower Parel',
      fullAddress: 'Senapati Bapat Marg, Mumbai',
      subtotal: 2400,
      serviceFee: 120,
      total: 2520,
      status: 'PENDING'
    }
  ]);

  // Worker availability
  const [workerAvailable, setWorkerAvailable] = useState(true);

  // Admin verification state
  const [pendingWorkers, setPendingWorkers] = useState<Worker[]>([
    {
      id: 'w4',
      name: 'Sunil Verma',
      category: 'Helper',
      rating: 0,
      reviewsCount: 0,
      hourlyRate: 200,
      experienceYears: 1,
      city: 'Mumbai',
      isVerified: false,
      isAvailable: true,
      bio: 'Energetic event setup helper.',
      skills: ['Table Setup', 'Loading/Unloading'],
      photoUrl: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200'
    }
  ]);

  const categories = ['All', 'Waiter', 'Butler', 'Captain', 'Supervisor', 'Bartender', 'Chef', 'Helper', 'Housekeeping'];

  // Vendor Onboarding Form State
  const [newVendorName, setNewVendorName] = useState('');
  const [newVendorCategory, setNewVendorCategory] = useState('Waiter');
  const [newVendorRate, setNewVendorRate] = useState('300');
  const [newVendorExp, setNewVendorExp] = useState('3');
  const [newVendorCity, setNewVendorCity] = useState('Mumbai');
  const [newVendorBio, setNewVendorBio] = useState('');
  const [newVendorSkills, setNewVendorSkills] = useState('Banquet Service, Fine Dining, Silverware');
  const [newVendorSuccessMsg, setNewVendorSuccessMsg] = useState('');

  // Aadhaar Verification State
  const [aadhaarInput, setAadhaarInput] = useState('4829 1042 8831');
  const [aadhaarOtpSent, setAadhaarOtpSent] = useState(false);
  const [aadhaarOtpInput, setAadhaarOtpInput] = useState('');
  const [vendorAadhaarVerified, setVendorAadhaarVerified] = useState(false);
  const [aadhaarVerifying, setAadhaarVerifying] = useState(false);

  // Handlers
  const handleVerifyOtp = () => {
    if (userRole === 'CUSTOMER') setCurrentScreen('CUSTOMER_HOME');
    else if (userRole === 'WORKER') setCurrentScreen('WORKER_DASHBOARD');
    else setCurrentScreen('ADMIN_DASHBOARD');
  };

  const handleSendAadhaarOtp = () => {
    const raw = aadhaarInput.replace(/\s/g, '');
    if (raw.length < 12) {
      alert('Please enter a valid 12-digit Aadhaar number');
      return;
    }
    setAadhaarVerifying(true);
    setTimeout(() => {
      setAadhaarVerifying(false);
      setAadhaarOtpSent(true);
    }, 1000);
  };

  const handleVerifyAadhaarOtp = () => {
    if (aadhaarOtpInput.length < 6) {
      alert('Please enter the 6-digit Aadhaar OTP');
      return;
    }
    setAadhaarVerifying(true);
    setTimeout(() => {
      setAadhaarVerifying(false);
      setVendorAadhaarVerified(true);
    }, 1200);
  };

  const handleAddVendor = (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (!newVendorName.trim()) return;

    const rawAadhaar = aadhaarInput.replace(/\s/g, '');
    const masked = rawAadhaar.length >= 4 ? `XXXX-XXXX-${rawAadhaar.slice(-4)}` : 'XXXX-XXXX-8821';

    const newWorker: Worker = {
      id: 'w-' + Date.now(),
      name: newVendorName.trim(),
      category: newVendorCategory || 'Waiter',
      rating: 4.8,
      reviewsCount: 1,
      hourlyRate: Number(newVendorRate) || 300,
      experienceYears: Number(newVendorExp) || 3,
      city: newVendorCity || 'Mumbai',
      isVerified: true,
      isAvailable: true,
      bio: newVendorBio.trim() || 'Experienced event & banquet staff specializing in fine dining, wedding receptions, and luxury guest hosting.',
      skills: newVendorSkills.split(',').map(s => s.trim()).filter(Boolean),
      photoUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200',
      aadhaarNumber: masked,
      isAadhaarVerified: true
    };

    setWorkers([newWorker, ...workers]);
    setNewVendorSuccessMsg(`Successfully registered ${newWorker.name} as an Aadhaar Verified ${newWorker.category} Vendor!`);
    setTimeout(() => setNewVendorSuccessMsg(''), 4000);

    setNewVendorName('');
    setNewVendorBio('');
    setVendorAadhaarVerified(false);
    setAadhaarOtpSent(false);
    setAadhaarOtpInput('');

    setCurrentScreen('WORKER_SEARCH');
    setSearchCategory(newWorker.category);
  };

  const handleCreateBooking = () => {
    if (!selectedWorker) return;
    const sub = selectedWorker.hourlyRate * 4 * staffQty;
    const fee = sub * 0.05;
    const newB: Booking = {
      id: 'BK-' + Math.floor(10000 + Math.random() * 90000),
      customerName: userName,
      workerName: selectedWorker.name,
      category: selectedWorker.category,
      eventDate: bookingDate,
      startTime: startTime,
      endTime: endTime,
      staffQuantity: staffQty,
      location: venue,
      fullAddress: address,
      subtotal: sub,
      serviceFee: fee,
      total: sub + fee,
      status: 'PENDING'
    };
    setBookings([newB, ...bookings]);
    setLastBookingId(newB.id);
    setCurrentScreen('BOOKING_SUCCESS');
  };

  return (
    <div className="min-h-screen bg-neutral-950 text-neutral-100 flex flex-col items-center justify-center p-2 sm:p-4 font-sans">
      {/* Top App Bar & Role Switcher */}
      <div className="w-full max-w-md bg-neutral-900 border border-neutral-800 rounded-t-2xl p-3 flex items-center justify-between shadow-lg">
        <div className="flex items-center gap-2">
          <span className="text-xl font-bold tracking-tight text-white">Fest</span>
          <span className="text-xl font-bold tracking-tight text-amber-400">Forge</span>
          <span className="text-xs bg-amber-400/20 text-amber-400 px-2 py-0.5 rounded-full font-semibold">
            Android App
          </span>
        </div>
        <div className="flex bg-neutral-800 p-1 rounded-lg text-xs font-semibold">
          <button
            onClick={() => { setUserRole('CUSTOMER'); setCurrentScreen('CUSTOMER_HOME'); }}
            className={`px-2.5 py-1 rounded-md transition ${userRole === 'CUSTOMER' ? 'bg-amber-400 text-neutral-950' : 'text-neutral-400'}`}
          >
            Customer
          </button>
          <button
            onClick={() => { setUserRole('WORKER'); setCurrentScreen('WORKER_DASHBOARD'); }}
            className={`px-2.5 py-1 rounded-md transition ${userRole === 'WORKER' ? 'bg-amber-400 text-neutral-950' : 'text-neutral-400'}`}
          >
            Worker
          </button>
          <button
            onClick={() => { setUserRole('ADMIN'); setCurrentScreen('ADMIN_DASHBOARD'); }}
            className={`px-2.5 py-1 rounded-md transition ${userRole === 'ADMIN' ? 'bg-amber-400 text-neutral-950' : 'text-neutral-400'}`}
          >
            Admin
          </button>
        </div>
      </div>

      {/* Main Mobile Screen Frame */}
      <div className="w-full max-w-md h-[740px] bg-neutral-900 border-x border-b border-neutral-800 rounded-b-2xl shadow-2xl overflow-hidden flex flex-col relative">
        {/* Status Bar */}
        <div className="bg-neutral-950 px-4 py-1.5 flex items-center justify-between text-[11px] font-mono text-neutral-400">
          <span>10:58</span>
          <span className="font-semibold text-amber-400">FestForge • com.rehan.festforge</span>
          <span>100% 🔋</span>
        </div>

        {/* Screen Content Router */}
        <div className="flex-1 overflow-y-auto">
          {/* SPLASH SCREEN */}
          {currentScreen === 'SPLASH' && (
            <div className="h-full bg-neutral-950 flex flex-col items-center justify-center p-6 text-center">
              <div className="w-16 h-16 bg-amber-400/20 rounded-2xl flex items-center justify-center text-amber-400 mb-4 border border-amber-400/30">
                <Users size={32} />
              </div>
              <h1 className="text-3xl font-extrabold text-white">
                Fest<span className="text-amber-400">Forge</span>
              </h1>
              <p className="text-xs text-neutral-400 font-semibold tracking-widest uppercase mt-1">
                Event Staff Booking Platform
              </p>
              <div className="mt-12">
                <button
                  onClick={() => setCurrentScreen('LOGIN')}
                  className="w-64 py-3 bg-amber-400 hover:bg-amber-300 text-neutral-950 font-bold rounded-xl shadow-lg transition"
                >
                  Enter Application
                </button>
              </div>
            </div>
          )}

          {/* LOGIN SCREEN */}
          {currentScreen === 'LOGIN' && (
            <div className="p-6 h-full flex flex-col justify-center bg-neutral-950">
              <h2 className="text-2xl font-bold text-white mb-1">Mobile Login</h2>
              <p className="text-xs text-neutral-400 mb-6">Enter your 10-digit mobile number to verify via Firebase OTP</p>

              <label className="text-xs font-semibold text-neutral-300 mb-1 block">Mobile Number</label>
              <div className="flex items-center bg-neutral-900 border border-neutral-800 rounded-xl px-3 py-2.5 mb-4 focus-within:border-amber-400">
                <span className="text-amber-400 font-bold text-sm mr-2">+91</span>
                <input
                  type="text"
                  maxLength={10}
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  className="bg-transparent text-white font-medium text-sm w-full outline-none"
                />
              </div>

              <button
                onClick={() => setCurrentScreen('OTP')}
                disabled={phone.length !== 10}
                className="w-full py-3 bg-amber-400 disabled:opacity-50 text-neutral-950 font-bold rounded-xl mt-2 transition"
              >
                Send OTP
              </button>
            </div>
          )}

          {/* OTP SCREEN */}
          {currentScreen === 'OTP' && (
            <div className="p-6 h-full flex flex-col justify-center bg-neutral-950">
              <h2 className="text-2xl font-bold text-white mb-1">Verify OTP</h2>
              <p className="text-xs text-neutral-400 mb-6">Sent to +91 {phone}</p>

              <label className="text-xs font-semibold text-neutral-300 mb-1 block">6-Digit OTP</label>
              <input
                type="text"
                maxLength={6}
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                className="bg-neutral-900 border border-neutral-800 text-center tracking-widest text-lg font-bold text-amber-400 rounded-xl p-3 mb-6 outline-none focus:border-amber-400"
              />

              <button
                onClick={handleVerifyOtp}
                className="w-full py-3 bg-amber-400 text-neutral-950 font-bold rounded-xl transition"
              >
                Verify & Proceed
              </button>
            </div>
          )}

          {/* CUSTOMER HOME */}
          {currentScreen === 'CUSTOMER_HOME' && (
            <div className="p-4 space-y-5">
              {/* Header */}
              <div className="flex items-center justify-between border-b border-neutral-800 pb-3">
                <div>
                  <h3 className="text-lg font-bold text-white">Hello, {userName} 👋</h3>
                  <p className="text-xs text-neutral-400">📍 {userCity} • Event Staff Marketplace</p>
                </div>
                <button
                  onClick={() => setCurrentScreen('BOOKING_HISTORY')}
                  className="p-2 bg-neutral-800 text-amber-400 rounded-xl border border-neutral-700"
                >
                  <FileText size={18} />
                </button>
              </div>

              {/* Search Trigger */}
              <div
                onClick={() => setCurrentScreen('WORKER_SEARCH')}
                className="bg-neutral-900 border border-neutral-800 rounded-xl p-3 flex items-center gap-3 cursor-pointer text-neutral-400 text-sm hover:border-amber-400/50"
              >
                <Search size={18} className="text-amber-400" />
                <span>Search waiters, bartenders, chefs...</span>
              </div>

              {/* Service Categories */}
              <div>
                <h4 className="text-sm font-bold text-neutral-200 mb-2">Event Staff Categories</h4>
                <div className="flex gap-2 overflow-x-auto pb-2">
                  {categories.filter(c => c !== 'All').map(cat => (
                    <button
                      key={cat}
                      onClick={() => { setSearchCategory(cat); setCurrentScreen('WORKER_SEARCH'); }}
                      className="bg-neutral-900 border border-neutral-800 rounded-xl p-3 min-w-[90px] flex flex-col items-center justify-center hover:border-amber-400/50 transition"
                    >
                      <div className="w-8 h-8 bg-amber-400/10 text-amber-400 font-bold rounded-lg flex items-center justify-center mb-1 text-xs">
                        {cat.slice(0, 2).toUpperCase()}
                      </div>
                      <span className="text-xs font-semibold text-neutral-200">{cat}</span>
                    </button>
                  ))}
                </div>
              </div>

              {/* Featured Staff */}
              <div>
                <div className="flex items-center justify-between mb-2">
                  <h4 className="text-sm font-bold text-neutral-200">Top Verified Staff</h4>
                  <button
                    onClick={() => { setNewVendorCategory('Waiter'); setCurrentScreen('WORKER_ONBOARDING'); }}
                    className="text-xs bg-amber-400/10 hover:bg-amber-400/20 text-amber-400 border border-amber-400/30 px-2.5 py-1 rounded-lg font-semibold flex items-center gap-1 transition"
                  >
                    <Plus size={14} /> Add Waiter
                  </button>
                </div>
                {newVendorSuccessMsg && (
                  <div className="bg-emerald-950/60 border border-emerald-500/40 text-emerald-300 text-xs p-2.5 rounded-xl mb-3 flex items-center gap-2">
                    <CheckCircle size={16} className="text-emerald-400 shrink-0" />
                    <span>{newVendorSuccessMsg}</span>
                  </div>
                )}
                <div className="space-y-3">
                  {workers.map(w => (
                    <div
                      key={w.id}
                      onClick={() => { setSelectedWorker(w); setCurrentScreen('WORKER_PROFILE'); }}
                      className="bg-neutral-900 border border-neutral-800 hover:border-amber-400/50 rounded-xl p-3 flex items-center gap-3 cursor-pointer transition"
                    >
                      <img src={w.photoUrl} alt={w.name} className="w-14 h-14 rounded-full object-cover border border-neutral-700" />
                      <div className="flex-1">
                        <div className="flex items-center gap-1.5">
                          <span className="font-bold text-sm text-white">{w.name}</span>
                          {w.isVerified && <CheckCircle size={14} className="text-amber-400" />}
                        </div>
                        <p className="text-xs text-neutral-400">{w.category} • {w.experienceYears} yrs exp</p>
                        <div className="flex items-center gap-1 mt-1 text-xs text-amber-400 font-semibold">
                          <Star size={12} fill="currentColor" />
                          <span>{w.rating} ({w.reviewsCount})</span>
                        </div>
                      </div>
                      <div className="text-right">
                        <span className="font-bold text-amber-400 text-sm">₹{w.hourlyRate}</span>
                        <span className="text-[10px] text-neutral-400 block">/hour</span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* WORKER SEARCH */}
          {currentScreen === 'WORKER_SEARCH' && (
            <div className="p-4 space-y-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <button onClick={() => setCurrentScreen('CUSTOMER_HOME')} className="p-2 text-neutral-400">
                    <ArrowLeft size={20} />
                  </button>
                  <h3 className="font-bold text-white text-base">Search Event Staff</h3>
                </div>
                <button
                  onClick={() => { setNewVendorCategory('Waiter'); setCurrentScreen('WORKER_ONBOARDING'); }}
                  className="text-xs bg-amber-400 text-neutral-950 px-2.5 py-1.5 rounded-lg font-bold flex items-center gap-1 shadow hover:bg-amber-300 transition"
                >
                  <Plus size={14} /> Add Vendor
                </button>
              </div>

              {/* Filter Pills */}
              <div className="flex gap-2 overflow-x-auto">
                {categories.map(cat => (
                  <button
                    key={cat}
                    onClick={() => setSearchCategory(cat)}
                    className={`px-3 py-1.5 rounded-lg text-xs font-semibold whitespace-nowrap ${searchCategory === cat ? 'bg-amber-400 text-neutral-950' : 'bg-neutral-800 text-neutral-300'}`}
                  >
                    {cat}
                  </button>
                ))}
              </div>

              <div className="space-y-3">
                {workers
                  .filter(w => searchCategory === 'All' || w.category === searchCategory)
                  .map(w => (
                    <div
                      key={w.id}
                      onClick={() => { setSelectedWorker(w); setCurrentScreen('WORKER_PROFILE'); }}
                      className="bg-neutral-900 border border-neutral-800 hover:border-amber-400/40 rounded-xl p-3 flex items-center gap-3 cursor-pointer transition"
                    >
                      <img src={w.photoUrl} alt={w.name} className="w-14 h-14 rounded-full object-cover border border-neutral-700" />
                      <div className="flex-1">
                        <div className="flex items-center gap-1.5 flex-wrap">
                          <span className="font-bold text-sm text-white">{w.name}</span>
                          {w.isVerified && <CheckCircle size={14} className="text-amber-400 shrink-0" />}
                          {w.isAadhaarVerified && (
                            <span className="text-[10px] bg-emerald-950/80 text-emerald-400 border border-emerald-500/40 px-1.5 py-0.2 rounded font-bold flex items-center gap-0.5">
                              <Shield size={10} /> Aadhaar ✓
                            </span>
                          )}
                        </div>
                        <p className="text-xs text-neutral-400">{w.category} • {w.city}</p>
                        <p className="text-xs text-amber-400 font-semibold">★ {w.rating} ({w.reviewsCount} reviews)</p>
                      </div>
                      <div className="text-right">
                        <span className="font-bold text-amber-400 text-sm">₹{w.hourlyRate}/hr</span>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          )}

          {/* WORKER PROFILE */}
          {currentScreen === 'WORKER_PROFILE' && selectedWorker && (
            <div className="p-4 space-y-4">
              <div className="flex items-center gap-3">
                <button onClick={() => setCurrentScreen('WORKER_SEARCH')} className="p-2 text-neutral-400 hover:text-white">
                  <ArrowLeft size={20} />
                </button>
                <h3 className="font-bold text-white text-base">{selectedWorker.name}</h3>
              </div>

              <div className="bg-neutral-900 border border-neutral-800 rounded-2xl p-4 text-center space-y-3">
                <img src={selectedWorker.photoUrl} alt={selectedWorker.name} className="w-20 h-20 rounded-full mx-auto object-cover border-2 border-amber-400 mb-2" />
                <h4 className="font-bold text-lg text-white flex items-center justify-center gap-1.5">
                  {selectedWorker.name} {selectedWorker.isVerified && <CheckCircle size={18} className="text-amber-400" />}
                </h4>
                <p className="text-xs text-neutral-400">{selectedWorker.category} • {selectedWorker.city}</p>

                {/* Aadhaar Verification Banner */}
                <div className="bg-neutral-950 border border-emerald-500/40 rounded-xl p-2.5 flex items-center justify-between text-left">
                  <div className="flex items-center gap-2">
                    <div className="w-8 h-8 rounded-full bg-emerald-500/20 text-emerald-400 flex items-center justify-center shrink-0">
                      <Shield size={16} />
                    </div>
                    <div>
                      <span className="text-xs font-bold text-emerald-400 flex items-center gap-1">
                        UIDAI Aadhaar Verified Staff <CheckCircle size={12} />
                      </span>
                      <span className="text-[10px] text-neutral-400 font-mono block">
                        Aadhaar Hash: {selectedWorker.aadhaarNumber || 'XXXX-XXXX-4921'}
                      </span>
                    </div>
                  </div>
                  <span className="text-[10px] bg-emerald-950 text-emerald-300 border border-emerald-500/30 px-2 py-0.5 rounded font-semibold">
                    KYC Cleared
                  </span>
                </div>

                <div className="grid grid-cols-3 gap-2 mt-4 pt-4 border-t border-neutral-800">
                  <div>
                    <span className="text-[10px] text-neutral-400 block">Rating</span>
                    <span className="font-bold text-amber-400 text-sm">★ {selectedWorker.rating}</span>
                  </div>
                  <div>
                    <span className="text-[10px] text-neutral-400 block">Experience</span>
                    <span className="font-bold text-white text-sm">{selectedWorker.experienceYears} Yrs</span>
                  </div>
                  <div>
                    <span className="text-[10px] text-neutral-400 block">Hourly Rate</span>
                    <span className="font-bold text-amber-400 text-sm">₹{selectedWorker.hourlyRate}/hr</span>
                  </div>
                </div>
              </div>

              <div className="bg-neutral-900 border border-neutral-800 rounded-xl p-4">
                <h5 className="text-xs font-bold text-neutral-300 uppercase mb-1">About Staff</h5>
                <p className="text-xs text-neutral-400 leading-relaxed">{selectedWorker.bio}</p>
              </div>

              <button
                onClick={() => setCurrentScreen('BOOKING')}
                className="w-full py-3 bg-amber-400 text-neutral-950 font-bold rounded-xl shadow-lg transition"
              >
                Book Now (₹{selectedWorker.hourlyRate}/hr)
              </button>
            </div>
          )}

          {/* BOOKING SCREEN */}
          {currentScreen === 'BOOKING' && selectedWorker && (
            <div className="p-4 space-y-4">
              <div className="flex items-center gap-3">
                <button onClick={() => setCurrentScreen('WORKER_PROFILE')} className="p-2 text-neutral-400">
                  <ArrowLeft size={20} />
                </button>
                <h3 className="font-bold text-white text-base">Event Requirements</h3>
              </div>

              <div className="space-y-3 text-xs">
                <div>
                  <label className="text-neutral-300 font-semibold block mb-1">Event Date</label>
                  <input value={bookingDate} onChange={e => setBookingDate(e.target.value)} className="w-full bg-neutral-900 border border-neutral-800 p-2.5 rounded-xl text-white outline-none" />
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-neutral-300 font-semibold block mb-1">Start Time</label>
                    <input value={startTime} onChange={e => setStartTime(e.target.value)} className="w-full bg-neutral-900 border border-neutral-800 p-2.5 rounded-xl text-white outline-none" />
                  </div>
                  <div>
                    <label className="text-neutral-300 font-semibold block mb-1">End Time</label>
                    <input value={endTime} onChange={e => setEndTime(e.target.value)} className="w-full bg-neutral-900 border border-neutral-800 p-2.5 rounded-xl text-white outline-none" />
                  </div>
                </div>
                <div>
                  <label className="text-neutral-300 font-semibold block mb-1">Staff Quantity</label>
                  <input type="number" min={1} value={staffQty} onChange={e => setStaffQty(Number(e.target.value))} className="w-full bg-neutral-900 border border-neutral-800 p-2.5 rounded-xl text-white outline-none" />
                </div>
                <div>
                  <label className="text-neutral-300 font-semibold block mb-1">Venue Name / Landmark</label>
                  <input value={venue} onChange={e => setVenue(e.target.value)} className="w-full bg-neutral-900 border border-neutral-800 p-2.5 rounded-xl text-white outline-none" />
                </div>
                <div>
                  <label className="text-neutral-300 font-semibold block mb-1">Full Address</label>
                  <input value={address} onChange={e => setAddress(e.target.value)} className="w-full bg-neutral-900 border border-neutral-800 p-2.5 rounded-xl text-white outline-none" />
                </div>
              </div>

              <button
                onClick={handleCreateBooking}
                className="w-full py-3 bg-amber-400 text-neutral-950 font-bold rounded-xl mt-4 transition"
              >
                Confirm & Request Booking
              </button>
            </div>
          )}

          {/* BOOKING SUCCESS */}
          {currentScreen === 'BOOKING_SUCCESS' && (
            <div className="p-6 h-full flex flex-col items-center justify-center text-center bg-neutral-950">
              <CheckCircle size={64} className="text-amber-400 mb-4" />
              <h3 className="text-2xl font-bold text-white">Booking Request Sent!</h3>
              <p className="text-sm font-semibold text-amber-400 mt-1">Ref ID: {lastBookingId}</p>
              <p className="text-xs text-neutral-400 mt-2 mb-8">Worker notified. You will track status in Booking History.</p>

              <button
                onClick={() => setCurrentScreen('BOOKING_HISTORY')}
                className="w-full py-3 bg-amber-400 text-neutral-950 font-bold rounded-xl transition"
              >
                View Booking History
              </button>
            </div>
          )}

          {/* BOOKING HISTORY */}
          {currentScreen === 'BOOKING_HISTORY' && (
            <div className="p-4 space-y-4">
              <div className="flex items-center gap-3">
                <button onClick={() => setCurrentScreen('CUSTOMER_HOME')} className="p-2 text-neutral-400">
                  <ArrowLeft size={20} />
                </button>
                <h3 className="font-bold text-white text-base">Booking History</h3>
              </div>

              <div className="space-y-3">
                {bookings.map(b => (
                  <div key={b.id} className="bg-neutral-900 border border-neutral-800 rounded-xl p-3 space-y-2">
                    <div className="flex justify-between items-center text-xs">
                      <span className="font-bold text-neutral-400">{b.id}</span>
                      <span className="bg-amber-400/20 text-amber-400 px-2 py-0.5 rounded font-bold">{b.status}</span>
                    </div>
                    <h4 className="font-bold text-sm text-white">{b.category} ({b.staffQuantity} Staff)</h4>
                    <p className="text-xs text-neutral-400">Worker: {b.workerName}</p>
                    <p className="text-xs text-neutral-400">📅 {b.eventDate} • 📍 {b.location}</p>
                    <div className="pt-2 border-t border-neutral-800 flex justify-between items-center">
                      <span className="text-xs font-bold text-amber-400">Total: ₹{b.total}</span>
                      <span className="text-[10px] text-neutral-400">Payment: Cash on Event</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* WORKER DASHBOARD */}
          {currentScreen === 'WORKER_DASHBOARD' && (
            <div className="p-4 space-y-4">
              <div className="flex items-center justify-between border-b border-neutral-800 pb-3">
                <div>
                  <h3 className="text-lg font-bold text-white">Worker Dashboard</h3>
                  <p className="text-xs text-neutral-400">Rajesh Kumar • Waiter</p>
                </div>
                <div className="flex items-center gap-2">
                  <span className="text-xs font-semibold text-neutral-300">{workerAvailable ? 'Online' : 'Offline'}</span>
                  <input
                    type="checkbox"
                    checked={workerAvailable}
                    onChange={e => setWorkerAvailable(e.target.checked)}
                    className="accent-amber-400 w-4 h-4"
                  />
                </div>
              </div>

              <div className="bg-neutral-900 border border-neutral-800 rounded-xl p-4 flex justify-between items-center">
                <div>
                  <span className="text-xs text-neutral-400 block">Today's Shift Earnings</span>
                  <span className="text-2xl font-bold text-amber-400">₹2,400</span>
                </div>
                <button className="px-3 py-1.5 bg-neutral-800 text-xs text-amber-400 font-bold rounded-lg">
                  Earnings
                </button>
              </div>

              <div>
              <div className="flex justify-between items-center mb-2">
                <h4 className="text-sm font-bold text-neutral-200">New Event Requests (1)</h4>
                <button
                  onClick={() => { setNewVendorCategory('Waiter'); setCurrentScreen('WORKER_ONBOARDING'); }}
                  className="text-xs bg-amber-400/20 text-amber-400 border border-amber-400/30 px-2.5 py-1 rounded-lg font-bold flex items-center gap-1 hover:bg-amber-400/30 transition"
                >
                  <Plus size={14} /> Add Waiter Vendor
                </button>
              </div>
                {bookings.map(b => (
                  <div key={b.id} className="bg-neutral-900 border border-neutral-800 rounded-xl p-3 space-y-2">
                    <div className="flex justify-between items-center">
                      <span className="text-xs font-bold text-amber-400">{b.id}</span>
                      <span className="text-xs text-neutral-400">₹{b.subtotal} Offer</span>
                    </div>
                    <p className="text-sm font-bold text-white">{b.category} Shift for {b.customerName}</p>
                    <p className="text-xs text-neutral-400">📅 {b.eventDate} ({b.startTime})</p>
                    <p className="text-xs text-neutral-400">📍 {b.fullAddress}</p>

                    <div className="flex gap-2 pt-2">
                      <button className="flex-1 py-1.5 bg-neutral-800 text-red-400 font-bold text-xs rounded-lg">
                        Reject
                      </button>
                      <button className="flex-1 py-1.5 bg-emerald-600 text-white font-bold text-xs rounded-lg">
                        Accept Shift
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* WORKER ONBOARDING / REGISTER WAITER VENDOR */}
          {currentScreen === 'WORKER_ONBOARDING' && (
            <div className="p-4 space-y-4">
              <div className="flex items-center gap-3 border-b border-neutral-800 pb-3">
                <button onClick={() => setCurrentScreen('CUSTOMER_HOME')} className="p-2 text-neutral-400 hover:text-white">
                  <ArrowLeft size={20} />
                </button>
                <div>
                  <h3 className="font-bold text-white text-base">Register Waiter / Staff Vendor</h3>
                  <p className="text-xs text-neutral-400">Onboard new event staff & catering vendors</p>
                </div>
              </div>

              <form onSubmit={handleAddVendor} className="space-y-3 text-xs">
                <div>
                  <label className="text-neutral-300 font-semibold block mb-1">Full Name / Vendor Name *</label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. Ramesh Deshmukh"
                    value={newVendorName}
                    onChange={e => setNewVendorName(e.target.value)}
                    className="w-full bg-neutral-900 border border-neutral-800 focus:border-amber-400 p-2.5 rounded-xl text-white outline-none"
                  />
                </div>

                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-neutral-300 font-semibold block mb-1">Primary Category</label>
                    <select
                      value={newVendorCategory}
                      onChange={e => setNewVendorCategory(e.target.value)}
                      className="w-full bg-neutral-900 border border-neutral-800 focus:border-amber-400 p-2.5 rounded-xl text-white outline-none"
                    >
                      {categories.filter(c => c !== 'All').map(c => (
                        <option key={c} value={c}>{c}</option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className="text-neutral-300 font-semibold block mb-1">Hourly Rate (₹)</label>
                    <input
                      type="number"
                      required
                      min={100}
                      value={newVendorRate}
                      onChange={e => setNewVendorRate(e.target.value)}
                      className="w-full bg-neutral-900 border border-neutral-800 focus:border-amber-400 p-2.5 rounded-xl text-white outline-none"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-neutral-300 font-semibold block mb-1">Years Experience</label>
                    <input
                      type="number"
                      required
                      min={0}
                      value={newVendorExp}
                      onChange={e => setNewVendorExp(e.target.value)}
                      className="w-full bg-neutral-900 border border-neutral-800 focus:border-amber-400 p-2.5 rounded-xl text-white outline-none"
                    />
                  </div>
                  <div>
                    <label className="text-neutral-300 font-semibold block mb-1">Operating City</label>
                    <input
                      type="text"
                      required
                      value={newVendorCity}
                      onChange={e => setNewVendorCity(e.target.value)}
                      className="w-full bg-neutral-900 border border-neutral-800 focus:border-amber-400 p-2.5 rounded-xl text-white outline-none"
                    />
                  </div>
                </div>

                <div>
                  <label className="text-neutral-300 font-semibold block mb-1">Key Skills (Comma Separated)</label>
                  <input
                    type="text"
                    value={newVendorSkills}
                    onChange={e => setNewVendorSkills(e.target.value)}
                    placeholder="Banquet Service, Fine Dining, Silverware"
                    className="w-full bg-neutral-900 border border-neutral-800 focus:border-amber-400 p-2.5 rounded-xl text-white outline-none"
                  />
                </div>

                <div>
                  <label className="text-neutral-300 font-semibold block mb-1">Bio / Catering Background</label>
                  <textarea
                    rows={3}
                    value={newVendorBio}
                    onChange={e => setNewVendorBio(e.target.value)}
                    placeholder="Experienced waiter specializing in large wedding receptions and corporate buffets..."
                    className="w-full bg-neutral-900 border border-neutral-800 focus:border-amber-400 p-2.5 rounded-xl text-white outline-none"
                  />
                </div>

                {/* Aadhaar Verification Section */}
                <div className="p-3 bg-neutral-900 border border-amber-400/30 rounded-xl space-y-2">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-1.5 text-amber-400 font-bold">
                      <Shield size={16} />
                      <span>UIDAI Aadhaar Instant Verification</span>
                    </div>
                    {vendorAadhaarVerified && (
                      <span className="text-[10px] bg-emerald-950 text-emerald-400 border border-emerald-500/40 px-2 py-0.5 rounded-full font-bold flex items-center gap-1">
                        <CheckCircle size={12} /> Aadhaar Verified
                      </span>
                    )}
                  </div>

                  {!vendorAadhaarVerified ? (
                    <div className="space-y-2 pt-1">
                      <div>
                        <label className="text-neutral-400 text-[11px] block mb-1">Enter 12-Digit Aadhaar Number</label>
                        <div className="flex gap-2">
                          <input
                            type="text"
                            maxLength={14}
                            placeholder="4829 1042 8831"
                            value={aadhaarInput}
                            onChange={e => {
                              const val = e.target.value.replace(/\D/g, '').slice(0, 12);
                              const formatted = val.replace(/(.{4})/g, '$1 ').trim();
                              setAadhaarInput(formatted);
                            }}
                            className="flex-1 bg-neutral-950 border border-neutral-800 focus:border-amber-400 p-2 rounded-lg text-white font-mono tracking-wider outline-none"
                          />
                          {!aadhaarOtpSent && (
                            <button
                              type="button"
                              onClick={handleSendAadhaarOtp}
                              disabled={aadhaarVerifying}
                              className="px-3 py-2 bg-amber-400 text-neutral-950 font-bold rounded-lg hover:bg-amber-300 transition text-[11px] whitespace-nowrap"
                            >
                              {aadhaarVerifying ? 'Sending...' : 'Get OTP'}
                            </button>
                          )}
                        </div>
                      </div>

                      {aadhaarOtpSent && (
                        <div className="bg-neutral-950 p-2.5 rounded-lg border border-neutral-800 space-y-2">
                          <div className="flex justify-between items-center text-[10px] text-neutral-400">
                            <span>OTP sent to Aadhaar linked mobile (******9821)</span>
                            <span className="text-amber-400 font-semibold">Valid for 10:00</span>
                          </div>
                          <div className="flex gap-2">
                            <input
                              type="text"
                              maxLength={6}
                              placeholder="Enter 6-digit OTP (123456)"
                              value={aadhaarOtpInput}
                              onChange={e => setAadhaarOtpInput(e.target.value.replace(/\D/g, ''))}
                              className="flex-1 bg-neutral-900 border border-neutral-700 focus:border-emerald-400 p-2 rounded-lg text-white text-center font-mono tracking-widest text-sm outline-none"
                            />
                            <button
                              type="button"
                              onClick={handleVerifyAadhaarOtp}
                              disabled={aadhaarVerifying}
                              className="px-3 py-2 bg-emerald-500 text-white font-bold rounded-lg hover:bg-emerald-400 transition text-[11px] whitespace-nowrap"
                            >
                              {aadhaarVerifying ? 'Verifying...' : 'Verify OTP'}
                            </button>
                          </div>
                        </div>
                      )}
                    </div>
                  ) : (
                    <div className="bg-neutral-950 p-2.5 rounded-lg border border-emerald-500/30 flex items-center gap-2.5">
                      <div className="w-8 h-8 rounded-full bg-emerald-500/20 text-emerald-400 flex items-center justify-center shrink-0">
                        <CheckCircle size={18} />
                      </div>
                      <div className="text-[11px]">
                        <span className="text-white font-bold block">UIDAI Identity Cleared</span>
                        <span className="text-neutral-400 block font-mono">Aadhaar No: XXXX-XXXX-{aadhaarInput.replace(/\s/g, '').slice(-4) || '8831'}</span>
                        <span className="text-emerald-400 text-[10px] block mt-0.5">Govt e-KYC Hash Verified ✓</span>
                      </div>
                    </div>
                  )}
                </div>

                <button
                  type="submit"
                  className="w-full py-3 bg-amber-400 text-neutral-950 font-bold rounded-xl shadow-lg hover:bg-amber-300 transition mt-2 flex items-center justify-center gap-1.5"
                >
                  <Shield size={16} /> Register & Publish Waiter Vendor
                </button>
              </form>
            </div>
          )}

          {/* ADMIN DASHBOARD */}
          {currentScreen === 'ADMIN_DASHBOARD' && (
            <div className="p-4 space-y-4">
              <div className="flex items-center justify-between border-b border-neutral-800 pb-3">
                <div>
                  <h3 className="text-lg font-bold text-white">FestForge Admin Console</h3>
                  <p className="text-xs text-neutral-400">Platform Management & Verification</p>
                </div>
                <button
                  onClick={() => { setNewVendorCategory('Waiter'); setCurrentScreen('WORKER_ONBOARDING'); }}
                  className="text-xs bg-amber-400 text-neutral-950 px-2.5 py-1.5 rounded-lg font-bold flex items-center gap-1 shadow hover:bg-amber-300 transition"
                >
                  <Plus size={14} /> Add Vendor
                </button>
              </div>

              <div className="grid grid-cols-2 gap-2 text-xs">
                <div className="bg-neutral-900 border border-neutral-800 p-3 rounded-xl">
                  <span className="text-neutral-400 block">Service Fee Revenue</span>
                  <span className="text-lg font-bold text-amber-400">₹1,420</span>
                </div>
                <div className="bg-neutral-900 border border-neutral-800 p-3 rounded-xl">
                  <span className="text-neutral-400 block">Pending Verifications</span>
                  <span className="text-lg font-bold text-white">{pendingWorkers.length}</span>
                </div>
              </div>

              <div className="space-y-2">
                <h4 className="text-sm font-bold text-neutral-200">Pending Identity Verifications</h4>
                {pendingWorkers.map(pw => (
                  <div key={pw.id} className="bg-neutral-900 border border-neutral-800 rounded-xl p-3 space-y-2 text-xs">
                    <div className="flex justify-between font-bold text-white">
                      <span>{pw.name}</span>
                      <span className="text-amber-400">{pw.category}</span>
                    </div>
                    <p className="text-neutral-400">City: {pw.city} • Rate: ₹{pw.hourlyRate}/hr</p>
                    
                    <div className="bg-neutral-950 p-2 rounded-lg border border-amber-400/30 flex items-center justify-between">
                      <span className="text-amber-400 font-bold flex items-center gap-1 text-[11px]">
                        <Shield size={14} /> Aadhaar OTP Proof Attached
                      </span>
                      <span className="text-emerald-400 font-mono text-[10px]">XXXX-XXXX-9812</span>
                    </div>

                    <div className="flex gap-2 pt-1">
                      <button
                        onClick={() => {
                          const approvedWorker = { ...pw, isVerified: true, isAadhaarVerified: true, aadhaarNumber: 'XXXX-XXXX-9812' };
                          setWorkers([approvedWorker, ...workers]);
                          setPendingWorkers(pendingWorkers.filter(p => p.id !== pw.id));
                        }}
                        className="flex-1 py-1.5 bg-emerald-600 hover:bg-emerald-500 text-white font-bold rounded-lg transition flex items-center justify-center gap-1"
                      >
                        <CheckCircle size={14} /> Approve & Mark Aadhaar Verified
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Bottom Android System Nav Bar */}
        <div className="bg-neutral-950 border-t border-neutral-800 px-8 py-2 flex items-center justify-around text-neutral-400">
          <button onClick={() => setCurrentScreen('SPLASH')} className="hover:text-amber-400 text-xs">
            ◀
          </button>
          <button
            onClick={() => {
              if (userRole === 'CUSTOMER') setCurrentScreen('CUSTOMER_HOME');
              else if (userRole === 'WORKER') setCurrentScreen('WORKER_DASHBOARD');
              else setCurrentScreen('ADMIN_DASHBOARD');
            }}
            className="hover:text-amber-400 text-xs"
          >
            ●
          </button>
          <button onClick={() => setCurrentScreen('BOOKING_HISTORY')} className="hover:text-amber-400 text-xs">
            ■
          </button>
        </div>
      </div>
    </div>
  );
}
